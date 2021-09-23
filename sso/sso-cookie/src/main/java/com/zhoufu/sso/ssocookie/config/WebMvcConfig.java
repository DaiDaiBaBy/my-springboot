package com.zhoufu.sso.ssocookie.config;

import com.zhoufu.sso.ssocookie.entity.User;
import com.zhoufu.sso.ssocookie.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 14:03
 * @description: 拦截器配置
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册一个拦截器将redisTemplate也初始化 拦截所有请求但排除/login   /login包括get和post请求
        registry.addInterceptor(new LoginInterceptor(redisTemplate)).addPathPatterns("/*").excludePathPatterns("/login");
    }
}
