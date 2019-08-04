package com.leyou.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.extendpojo.SpuExt;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;


public interface SpuService {
    PageResult<SpuExt> querySpuPage(Integer page, Integer rows, Boolean saleable, String key);

    SpuDetail querySpuDetailById(Long spuId);
 }
