package com.leyou.service.impl;

import com.leyou.item.extendpojo.SpuExt;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.Stock;
import com.leyou.mapper.SkuMapper;
import com.leyou.mapper.SpuDetailMapper;
import com.leyou.mapper.SpuMapper;
import com.leyou.mapper.StockMapper;
import com.leyou.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    public static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    @Transactional
    public boolean saveGoodsinfo(SpuExt spu) {
        spu.setValid(true);
        spu.setSaleable(true);
        spu.setCreateTime(new Date());
        spu.setLastUpdateTime(spu.getCreateTime());
        spuMapper.insert(spu);
        spu.getSpuDetail().setSpuId(spu.getId());
        spuDetailMapper.insert(spu.getSpuDetail());

        try {
            saveSkuAndStock(spu.getSkus(), spu.getId());
            //通知其他服务 有商品信息插入
            sendMessage(spu.getId(),"insert");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public List<Sku> querySkuListById(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        List<Sku> skuList = skuMapper.select(sku);
        for (Sku oneSku : skuList) {
            oneSku.setStock(stockMapper.selectByPrimaryKey(oneSku.getId()).getStock());
        }
        return skuList;
    }

    @Override
    @Transactional
    public void updateGoodsinfo(SpuExt spu) {
        // 查询以前sku
        List<Sku> skus = querySkuListById(spu.getId());
        // 如果以前存在，则删除
        if (!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(
                    s -> s.getId()
            ).collect(Collectors.toList());
            // 删除以前库存
            Example example = new Example(Stock.class);
            example.createCriteria().andIn("skuId", ids);
            stockMapper.deleteByExample(example);

            // 删除以前的sku
            Sku record = new Sku();
            record.setSpuId(spu.getId());
            skuMapper.delete(record);

        }
        // 新增sku和库存
        saveSkuAndStock(spu.getSkus(), spu.getId());

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        spuMapper.updateByPrimaryKeySelective(spu);

        // 更新spu详情
        spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        //通知其他服务 有商品信息更新
        sendMessage(spu.getId(),"update");
    }

    @Override
    @Transactional
    public void deleteGoods(Long spuId) {
        List<Sku> skus=querySkuListById(spuId);
        deleteSkuAndStock(skus);
        spuDetailMapper.deleteByPrimaryKey(spuId);
        spuMapper.deleteByPrimaryKey(spuId);
    }

    @Override
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }


    private void saveSkuAndStock(List<Sku> skus, Long spuId){
        for(Sku sku:skus){
            if(!sku.getEnable()){
                continue;
            }
            sku.setSpuId(spuId);
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            int res = skuMapper.insert(sku);

            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setSeckillStock(sku.getStock());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        }
    }

    private void  deleteSkuAndStock(List<Sku> skus){
        for(Sku sku:skus){
            stockMapper.deleteByPrimaryKey(sku.getId());
            skuMapper.deleteByPrimaryKey(sku.getId());
        }
    }

    /**
     * 向消息队列发送消息
     * @param id
     * @param type
     */
    private void sendMessage(Long id,String type){
        try {
            this.amqpTemplate.convertAndSend("item."+type,id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }

}
