package com.leyou.cart.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    static final String KEY_PREFIX = "ly:cart:uid:";

    static final Logger logger = LoggerFactory.getLogger(CartService.class);

    public void addCart(Cart cart){

        UserInfo userInfo = LoginInterceptor.getLoginUser();
        String key = KEY_PREFIX+ userInfo.getId();
        BoundHashOperations<String,Object,Object> hashOperations = redisTemplate.boundHashOps(key);
        // 查询是否存在
        Long skuId=cart.getSkuId();
        Integer num =cart.getNum();
        Boolean boo = hashOperations.hasKey(skuId.toString());
        if(boo){
            String json = hashOperations.get(skuId.toString()).toString();
            cart = JsonUtils.parse(json,Cart.class);
            cart.setNum(cart.getNum()+num);
        }else {
            // 不存在，新增购物车数据
            cart.setUserId(userInfo.getId());
            // 其它商品信息， 需要查询商品服务
            Sku sku = this.goodsClient.querySkuById(skuId);
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(), ",")[0]);
            cart.setPrice(sku.getPrice());
            cart.setTitle(sku.getTitle());
            cart.setOwnSpec(sku.getOwnSpec());
        }
         // 将购物车数据写入redis
        hashOperations.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    /***
     * 获取购物车列表
     * @return
     */
    public List<Cart> queryCartList(){
        // 获取登录用户
        UserInfo user = LoginInterceptor.getLoginUser();

        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        if(!this.redisTemplate.hasKey(key)){
            // 不存在，直接返回
            return null;
        }
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        List<Object> carts = hashOps.values();
        // 判断是否有数据
        if(CollectionUtils.isEmpty(carts)){
            return null;
        }
        // 查询购物车数据
        return carts.stream().map(o -> JsonUtils.parse(o.toString(), Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(Long skuId, Integer num) {
        UserInfo user = LoginInterceptor.getLoginUser();

        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        String json = hashOps.get(skuId.toString()).toString();

        Cart cart=JsonUtils.parse(json,Cart.class);
        cart.setNum(num);
        hashOps.put(skuId.toString(),JsonUtils.serialize(cart));
    }

    public void deleteCart(String skuId) {
        //获取登录的用户信息
        UserInfo user = LoginInterceptor.getLoginUser();
        // 判断是否存在购物车
        String key = KEY_PREFIX + user.getId();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        hashOps.delete(skuId.toString());
    }
}
