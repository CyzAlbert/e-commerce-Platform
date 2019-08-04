package com.leyou.seckill.controller;


import com.leyou.auth.entity.UserInfo;
import com.leyou.item.pojo.SeckillGoods;
import com.leyou.seckill.access.AccessLimit;
import com.leyou.seckill.client.GoodsClient;
import com.leyou.seckill.interceptor.LoginInterceptor;
import com.leyou.seckill.pojo.SeckillMessage;
import com.leyou.seckill.service.SeckillService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class SeckillController implements InitializingBean {
    @Autowired
    private SeckillService seckillService;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX="leyou:seckill:stock";

    private Map<Long,Boolean> localMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        BoundHashOperations<String,Object,Object> hashOperations=redisTemplate.boundHashOps(KEY_PREFIX);
        if(hashOperations.hasKey(KEY_PREFIX)){
            hashOperations.entries().forEach((m,n)->{
                localMap.put(Long.parseLong(m.toString()),false);
            });
        }
    }

    @PostMapping("/{path}/seck")
    public ResponseEntity<String> seckillOrder(@PathVariable("path")String path, @RequestBody SeckillGoods seckillGoods){
        String result = "排队中";

        UserInfo userInfo = LoginInterceptor.getLoginUser();

        //1.验证路径
        boolean check = this.seckillService.checkSeckillPath(seckillGoods.getId(),userInfo.getId(),path);
        if (!check){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        //2.内存标记，减少redis访问
        boolean over = localMap.get(seckillGoods.getSkuId());
        if (over){
            return ResponseEntity.ok(result);
        }

        //3.读取库存，减一后更新缓存
        BoundHashOperations<String,Object,Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX);
        Long stock = hashOperations.increment(seckillGoods.getSkuId().toString(), -1);

        //4.库存不足直接返回
        if (stock < 0){
            localMap.put(seckillGoods.getSkuId(),true);
            return ResponseEntity.ok(result);
        }

        //5.库存充足，请求入队
        //5.1 获取用户信息
        SeckillMessage seckillMessage = new SeckillMessage(userInfo,seckillGoods);
        //5.2 发送消息
        this.seckillService.sendMessage(seckillMessage);

        return ResponseEntity.ok(result);
    }


    /**
     * 根据userId查询订单号
     * @param userId
     * @return
     */
    @GetMapping("orderId")
    public ResponseEntity<Long> checkSeckillOrder(Long userId){
        Long result = this.seckillService.checkSeckillOrder(userId);
        if (result == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(result);

    }

    /**
     * 创建秒杀路径
     * @param goodsId
     * @return
     */
    @AccessLimit(seconds = 20,maxCount = 5,needLogin = true)
    @GetMapping("get_path/{goodsId}")
    public ResponseEntity<String> getSeckillPath(@PathVariable("goodsId") Long goodsId){
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        if (userInfo == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String str = this.seckillService.createPath(goodsId,userInfo.getId());
        if (StringUtils.isEmpty(str)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(str);
    }
}
