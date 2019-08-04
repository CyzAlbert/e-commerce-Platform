package com.leyou.seckill.client;

import com.leyou.order.api.OrderApi;
import com.leyou.order.pojo.Order;
import com.leyou.seckill.config.OrderConfig;
import org.springframework.cloud.openfeign.FeignClient;


// Feign 调用不会转发请求头信息 在OrderConfig中转发请求头
@FeignClient(value = "order-service",configuration = OrderConfig.class)
public interface OrderClient extends OrderApi {
}
