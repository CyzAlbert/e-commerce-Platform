package com.leyou.order.config;

import com.leyou.order.properties.PayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * @author: 98050
 * @create: 2018-10-27
 **/


@Configuration
@EnableConfigurationProperties(PayProperties.class)
public class PayConfig {

    @Autowired
    private PayProperties payProperties;

    public String getAppID() {
        return payProperties.getAppId();
    }

    public String getMchID() {
        return payProperties.getMchId();
    }

    public String getKey() {
        return payProperties.getKey();
    }

    public InputStream getCertStream() {
        return null;
    }

    public int getHttpConnectTimeoutMs() {
        return payProperties.getConnectTimeoutMs();
    }

    public int getHttpReadTimeoutMs() {
        return payProperties.getReadTimeoutMs();
    }

}

