package com.leyou.item.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    @Column(name = "seckill_stock")
    private Long seckillStock;// 秒杀可用库存
    @Column(name = "seckill_total")
    private Long seckillTotal;// 已秒杀数量
    private Long stock;// 正常库存

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getSeckillStock() {
        return seckillStock;
    }

    public void setSeckillStock(Long seckillStock) {
        this.seckillStock = seckillStock;
    }

    public Long getSeckillTotal() {
        return seckillTotal;
    }

    public void setSeckillTotal(Long seckillTotal) {
        this.seckillTotal = seckillTotal;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }
}


