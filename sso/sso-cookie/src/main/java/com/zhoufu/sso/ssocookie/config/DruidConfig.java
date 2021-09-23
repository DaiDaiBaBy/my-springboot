package com.zhoufu.sso.ssocookie.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 12:00
 * @description: druid数据监控
 */
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }
    // 配置 druid的监控

    // 1. 配置一个管理后台的 Servlet
    @Bean
    public ServletRegistrationBean<StatViewServlet> statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", "admin");
        initParams.put("loginPassword", "admin");
        initParams.put("resetEnable", "false"); // 禁止HTML页面上的 ”Reset All“功能
        initParams.put("allow", ""); // IP 白名单（没有配置或者为空，则允许所有访问）
//        initParams.put("deny", "192.160.32.38");// IP 黑名单， （存在共同时， deny 优先级高于allow）
        bean.setInitParameters(initParams);
        return bean;
    }

    // 2. 配置一个web 监控的filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>();

        // 配置 哪些请求忽略
        initParams.put("exclusions", "*.js, *.gif, *.jpg, *.png, *.css, *.ico, /druid/*");
        bean.setInitParameters(initParams);
        // 添加过滤规则
        bean.setUrlPatterns(Collections.singletonList("/*"));
        return bean;
    }
}
