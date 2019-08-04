package com.leyou.order.config;

import com.leyou.common.utils.IdWorker;
import com.leyou.order.properties.IdWorkerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: 98050
 * @create: 2018-10-27
 **/
@Configuration
@EnableConfigurationProperties(IdWorkerProperties.class)
public class IdWorkerConfig {

    @Autowired
    private IdWorkerProperties idWorkerProperties;

    @Bean
    public IdWorker idWorker() {
        return new IdWorker(idWorkerProperties.getWorkerId(), idWorkerProperties.getDataCenterId());
    }
}
