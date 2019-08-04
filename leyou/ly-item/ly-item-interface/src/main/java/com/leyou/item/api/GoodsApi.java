package com.leyou.item.api;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.extendpojo.SpuExt;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface GoodsApi {
    /**
     * 分页查询商品
     * @param page
     * @param rows
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<SpuExt> querySpuPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable,
            @RequestParam(value = "key", required = false) String key);

    /**
     * 根据spuid 查询 spuDetail
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailById(@PathVariable("id") Long id);


    /**
     * 根据spuid 查询sku
     * @param id
     * @return
     */
    @GetMapping("/sku/list")
    List<Sku> querySkuListById(@RequestParam(name = "id") Long id);

    /**
     * 根据spuid 查询 spu
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);


    /**
     * 根据skuid 查询SKU
     * @param id
     * @return
     */
    @GetMapping("/sku/{id}")
    Sku querySkuById(@PathVariable("id") Long id);
}
