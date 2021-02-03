package com.zhoufu.springbootaccesscontrol;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: zhoufu
 * @Date: 2021/2/3 18:04
 * @description:   拦截器注册到  springboot
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private AccessLimitInterceptor interceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(interceptor);
    }
}
