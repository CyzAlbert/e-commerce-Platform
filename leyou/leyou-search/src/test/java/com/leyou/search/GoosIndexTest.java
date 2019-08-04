package com.leyou.search;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.extendpojo.SpuExt;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.repository.GoodsRepository;
import com.leyou.search.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LySearchService.class)
public class GoosIndexTest {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private SearchService searchService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void loadData(){
        //创建索引
        elasticsearchTemplate.createIndex(Goods.class);
        //配置映射
        elasticsearchTemplate.putMapping(Goods.class);
        int page=1;
        int rows=100;
        int size=0;
        while (size<100){
            PageResult<SpuExt> result = this.goodsClient.querySpuPage(page,rows,true,null);
            List<SpuExt> spuExts = result.getItems();
            size=spuExts.size();
            List<Goods> goods=new ArrayList<>();
            for(SpuExt spuExt:spuExts){
                try {
                    Goods oneGood = this.searchService.buildGoods(spuExt);
                    goods.add(oneGood);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
            goodsRepository.saveAll(goods);
            page++;
        }

    }
}
