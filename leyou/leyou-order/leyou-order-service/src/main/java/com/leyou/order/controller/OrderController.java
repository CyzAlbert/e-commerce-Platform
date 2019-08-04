package com.leyou.order.controller;

import com.leyou.common.pojo.PageResult;
import com.leyou.order.pojo.Order;
import com.leyou.order.pojo.OrderStatus;
import com.leyou.order.service.OrderService;
import com.leyou.order.utils.PayHelper;

import com.leyou.order.utils.PayState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: 98050
 * @Time: 2018-10-27 16:30
 * @Feature: 订单Controller
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayHelper payHelper;

    private static int count = 0;

    /**
     * 创建订单
     * @param order 订单对象
     * @return 订单编号
     */
    @PostMapping
    public ResponseEntity<List<Long>> createOrder(@RequestBody @Valid Order order){
        List<Long> skuId = this.orderService.queryStock(order);
        if (skuId.size() != 0){
            //库存不足
            return new ResponseEntity<>(skuId,HttpStatus.OK);
        }

        Long id = this.orderService.createOrder(order);
        return new ResponseEntity<>(Arrays.asList(id), HttpStatus.CREATED);
    }


    /**
     * 查询订单
     * @param id 订单编号
     * @return 订单对象
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id){
        System.out.println("查询订单："+id);
        Order order = this.orderService.queryOrderById(id);
        if (order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(order);
    }

    /**
     * 分页查询当前已经登录的用户订单
     * @param page 页数
     * @param rows 每页大小
     * @param status 订单状态
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<Order>> queryUserOrderList(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "status",required = false)Integer status
    ){

        PageResult<Order> result = this.orderService.queryUserOrderList(page,rows,status);
        if (result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }


    /**
     * 更新订单状态
     * @param id
     * @param status
     * @return
     */
    @PutMapping("{id}/{status}")
    public ResponseEntity<Boolean> updateOrderStatus(@PathVariable("id") Long id,@PathVariable("status") Integer status){
        Boolean result = this.orderService.updateOrderStatus(id,status);
        if (!result){
            //返回400
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //返回204
        return new ResponseEntity<>(result,HttpStatus.NO_CONTENT);
    }

    /**
     * 根据订单id生成付款链接
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    public ResponseEntity<String> generateUrl(@PathVariable("id") Long orderId){
        String url = this.payHelper.createPayUrl(orderId);
        if (StringUtils.isNotBlank(url)){
            return ResponseEntity.ok(url);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 查询付款状态
     * @param orderId
     * @return
     */
    @GetMapping("state/{id}")
    public ResponseEntity<Integer> queryPayState(@PathVariable("id") Long orderId){
        PayState payState = this.payHelper.queryOrder(orderId);
        return ResponseEntity.ok(payState.getValue());
    }

    /**
     * 根据订单id查询其包含的skuId
     * @param id
     * @return
     */
    @GetMapping("skuId/{id}")
    public ResponseEntity<List<Long>> querySkuIdByOrderId(@PathVariable("id") Long id){
        List<Long> longList = this.orderService.querySkuIdByOrderId(id);
        if (longList == null || longList.size() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(longList);
    }


    /**
     * 根据订单id查询订单状态
     * @param id
     * @return
     */
    @GetMapping("status/{id}")
    public ResponseEntity<OrderStatus> queryOrderStatusById(@PathVariable("id") Long id){
        OrderStatus orderStatus = this.orderService.queryOrderStatusById(id);
        if (orderStatus == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(orderStatus);
    }
}
