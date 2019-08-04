package com.leyou.seckill.access;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 秒杀接口限流
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    //限流时间
    int seconds();

    //最大请求次数
    int maxCount();

    //是否需要登录
    boolean needLogin() default true;
}
