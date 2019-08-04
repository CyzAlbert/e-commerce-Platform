package com.leyou.seckill.config;


import com.leyou.seckill.interceptor.AccessInterceptor;
import com.leyou.seckill.interceptor.LoginInterceptor;
import com.leyou.seckill.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebMvc
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Bean
    public LoginInterceptor loginInterceptor() {
        return new LoginInterceptor(jwtProperties);
    }

    @Autowired
    public AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePath = new ArrayList<>();
        excludePath.add("/list");
        excludePath.add("/addSeckill");
        registry.addInterceptor(loginInterceptor())
                .addPathPatterns("/**").excludePathPatterns(excludePath);
        registry.addInterceptor(accessInterceptor);
    }

}
