package com.leyou.seckill.pojo;

import com.leyou.auth.entity.UserInfo;
import com.leyou.item.pojo.SeckillGoods;

/**
 * 秒杀信息记录
 */
public class SeckillMessage {

    private UserInfo userInfo;

    private SeckillGoods seckillGoods;

    public SeckillMessage(UserInfo userInfo,SeckillGoods seckillGoods){
        this.seckillGoods=seckillGoods;
        this.userInfo=userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public SeckillGoods getSeckillGoods() {
        return seckillGoods;
    }

    public void setSeckillGoods(SeckillGoods seckillGoods) {
        this.seckillGoods = seckillGoods;
    }
}
