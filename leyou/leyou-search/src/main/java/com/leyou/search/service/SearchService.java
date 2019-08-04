package com.leyou.search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.*;
import com.leyou.search.client.BrandClient;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecificationClient;
import com.leyou.search.pojo.Goods;

import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.viewobjects.SearchRequest;
import com.leyou.search.viewobjects.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private SpecificationClient specificationClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * 将spu 信息转换成 Goods 方便存入索引库
     * @param spu
     * @return
     * @throws IOException
     */
    public Goods buildGoods(Spu spu) throws IOException{
        Goods goods = new Goods();
        List<String> categoryNames = this.categoryClient.queryNameByIds(
                Arrays.asList(spu.getCid1(),spu.getCid2(),spu.getCid3()));

        // 查询 sku
        List<Sku> skus =this.goodsClient.querySkuListById(spu.getId());

        // 查询 spu 详情
        SpuDetail spuDetail =this.goodsClient.querySpuDetailById(spu.getId());

        //查询 规格参数
        List<SpecParam> specParams=this.specificationClient.queryParamList(spu.getId(),spu.getCid3(),true);

        List<Long> prices = new ArrayList<>();
        List<Map<String,Object>> skuList=new ArrayList<>();
        skus.forEach(sku -> {
            prices.add(sku.getPrice());
            Map<String,Object> skuMap = new HashMap<>();
            skuMap.put("id",sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("price", sku.getPrice());
            skuMap.put("image", StringUtils.substringBefore(sku.getImages(),","));
            skuList.add(skuMap);
        });

        // 规格参数处理
        Map<String, Object> genericSpec = mapper.readValue(spuDetail.getGenericSpec(),
                new TypeReference<Map<String, Object>>() {});
        Map<String, Object> specialSpecs = mapper.readValue(spuDetail.getSpecialSpec(),
                new TypeReference<Map<String, Object>>() {});

        Map<String,Object> specMap = new HashMap<>();

        specParams.forEach(specParam->{
            boolean flag= specParam.getSearching()==1?true:false;
            if(flag){
                flag = specParam.getGeneric()==1?true:false;
                if(flag){
                    String value = genericSpec.get(specParam.getId().toString()).toString();
                    flag = specParam.getIsNum()==1?true:false;
                    if(flag){
                        value =chooseSegment(value,specParam);
                    }
                    specMap.put(specParam.getName(),StringUtils.isEmpty(value)?"其他":value);
                }else{
                    specMap.put(specParam.getName(),specialSpecs.get(specParam.getId().toString()));
                }
            }
        });

        goods.setId(spu.getId());
        goods.setSubTitle(spu.getSubTitle());
        goods.setBrandId(spu.getBrandId());
        goods.setCid1(spu.getCid1());
        goods.setCid2(spu.getCid2());
        goods.setCid3(spu.getCid3());
        goods.setCreateDate(spu.getCreateTime());
        goods.setAll(spu.getTitle() + " " + StringUtils.join(categoryNames," "));
        goods.setPrice(prices);
        goods.setSkus(mapper.writeValueAsString(skuList));
        goods.setSpecs(specMap);
        return goods;
    }


    private String chooseSegment(String value,SpecParam specParam){
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : specParam.getSegments().split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + specParam.getUnit() + "以上";
                }else if(begin == 0){
                    result = segs[1] + specParam.getUnit() + "以下";
                }else{
                    result = segment + specParam.getUnit();
                }
                break;
            }
        }
        return result;
    }

    public PageResult<Goods> search(SearchRequest searchRequest){
        logger.info(String.format("search parameters ....\n%s",searchRequest.toString()));
        String key = searchRequest.getKey();
        if(StringUtils.isEmpty(key)){
            return null;
        }

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 基本查询
        QueryBuilder basicQuery = buildBasicQueryWithFilter(searchRequest);
        queryBuilder.withQuery(basicQuery);

        // source 过滤 只需要 "id","skus","subTitle"
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","skus","subTitle"},null));

        // 分页排序
        searchWithPageAndSort(queryBuilder,searchRequest);

        // 商品分类聚合查询

        // 聚合
        String categoryAggName = "category"; // 商品分类聚合名称
        String brandAggName = "brand"; // 品牌聚合名称
        // 根据商品分类聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(categoryAggName).field("cid3"));
        // 根据品牌id聚合
        queryBuilder.addAggregation(AggregationBuilders.terms(brandAggName).field("brandId"));

        AggregatedPage<Goods> pageInfo=(AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());

        Long total = pageInfo.getTotalElements();
        int totalPage = (total.intValue() +  searchRequest.getSize() - 1) / searchRequest.getSize();

        // 获取商品分类的聚合结果
        List<Category> categories = getCategoryAggResult(pageInfo.getAggregation(categoryAggName));
        // 获取品牌聚合的结果
        List<Brand> brands = getBrandAggResult(pageInfo.getAggregation(brandAggName));
        //
        List<Map<String,Object>> specs = new ArrayList<>();

        if(null!=categories && categories.size()==1){
            // 如果商品分类只有一个才进行聚合，并根据分类与基本查询条件聚合
            specs = getSpecs(categories.get(0).getId(),basicQuery);
        }
        return new SearchResult(pageInfo.getTotalElements(),
                (long)pageInfo.getTotalPages(),
                pageInfo.getContent(),
                categories,brands,specs);
    }

    // 解析品牌聚合结果
    private List<Brand> getBrandAggResult(Aggregation aggregation) {
        try {
            LongTerms brandAgg = (LongTerms) aggregation;
            List<Long> brandIds = new ArrayList<>();
            for (LongTerms.Bucket bucket : brandAgg.getBuckets()) {
                brandIds.add(bucket.getKeyAsNumber().longValue());
            }
            // 根据id查询品牌
            return this.brandClient.queryBrandsByIds(brandIds);
        } catch (Exception e){
            logger.error("品牌聚合出现异常：", e);
            return null;
        }
    }

    // 解析商品分类聚合结果
    private List<Category> getCategoryAggResult(Aggregation aggregation) {
        try{
            List<Category> categories = new ArrayList<>();
            LongTerms categoryAgg = (LongTerms) aggregation;
            List<Long> categoryIds = new ArrayList<>();
            for (LongTerms.Bucket bucket : categoryAgg.getBuckets()) {
                categoryIds.add(bucket.getKeyAsNumber().longValue());
            }
            // 根据id查询分类名称
            List<String> names = this.categoryClient.queryNameByIds(categoryIds);

            for (int i = 0; i < names.size(); i++) {
                Category c = new Category();
                c.setId(categoryIds.get(i));
                c.setName(names.get(i));
                categories.add(c);
            }
            return categories;
        } catch (Exception e){
            logger.error("分类聚合出现异常：", e);
            return null;
        }
    }

    // 聚合规格参数
    private List<Map<String,Object>> getSpecs(Long cid, QueryBuilder queryBuilder){

        try {
            List<SpecParam> params = this.specificationClient.queryParamList(null,cid,true);

            List<Map<String,Object>> specs = new ArrayList<>();

            NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
            builder.withQuery(queryBuilder);

            params.forEach(param->{
                String key = param.getName();
                builder.addAggregation(AggregationBuilders.terms(key).field("specs."+key+".keyword"));
            });

            Map<String,Aggregation> aggregation = this.elasticsearchTemplate.query(builder.build(),
                    SearchResponse::getAggregations).asMap();
            params.forEach(param-> {
                        try {
                            Map<String, Object> spec = new HashMap<>();
                            String key = param.getName();
                            spec.put("k", key);
                            StringTerms terms = (StringTerms) aggregation.get(key);
                            spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                            specs.add(spec);
                        }catch (Exception e){
                            logger.error("exception when add specs");
                        }
                    });
            return specs;
        } catch (Exception e) {
            logger.error("规格参数聚合异常",e);
            return null;
        }
    }

    // 构建基本查询条件
    private void searchWithPageAndSort(NativeSearchQueryBuilder queryBuilder, SearchRequest request) {
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();

        // 1、分页
        queryBuilder.withPageable(PageRequest.of(page - 1, size));
        // 2、排序
        String sortBy = request.getSortBy();
        Boolean desc = request.getDescending();
        if (StringUtils.isNotBlank(sortBy)) {
            // 如果不为空，则进行排序
            queryBuilder.withSort(SortBuilders.fieldSort(sortBy).order(desc ? SortOrder.DESC : SortOrder.ASC));
        }
    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRequest request) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        // 基本查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", request.getKey()).operator(Operator.AND));
        // 过滤条件构建器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        // 整理过滤条件
        Map<String, String> filter = request.getFilter();
        if(null==filter){
            return queryBuilder;
        }
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 商品分类和品牌要特殊处理
            if (key != "cid3" && key != "brandId") {
                key = "specs." + key + ".keyword";
            }
            // 字符串类型，进行term查询
            filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
        }
        // 添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

    /**
     * 保存商品信息到索引库
     * @param id
     * @throws IOException
     */
    public void createIndex(Long id) throws IOException{
        Spu spu = this.goodsClient.querySpuById(id);

        Goods goods = this.buildGoods(spu);

        this.goodsRepository.save(goods);
    }

    /**
     * 删除索引
     */
    public void deleteIndex(Long id){
        this.goodsRepository.deleteById(id);
    }
}
