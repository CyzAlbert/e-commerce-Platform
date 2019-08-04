package com.leyou.seckill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(value = "com.leyou.seckill.mapper")
public class LySeckill {
    public static void main(String[] args){
        SpringApplication.run(LySeckill.class,args);
    }
}
