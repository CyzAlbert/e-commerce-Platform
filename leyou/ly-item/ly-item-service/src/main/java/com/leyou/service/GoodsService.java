package com.leyou.service;

import com.leyou.item.extendpojo.SpuExt;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;

import java.util.List;

public interface GoodsService {
    boolean saveGoodsinfo(SpuExt spu);

    List<Sku> querySkuListById(Long spuId);

    void updateGoodsinfo(SpuExt spu);

    void deleteGoods(Long spuId);

    Spu querySpuById(Long id);

    Sku querySkuById(Long id);
}
