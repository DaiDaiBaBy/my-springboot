package com.zhoufu.springbootshiromybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: zhoufu
 * @Date: 2021/3/1 14:46
 * @description:
 */
@Configuration
@EnableCaching  // 缓存功能注解
@Slf4j
public class RedisConfig extends CachingConfigurerSupport{
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.pool-max-wait}")
    private long maxWaitMillis;

    @Bean
    public JedisPool redisPoolFactory(){
        log.info("JedisPool注入成功！！:{}", getClass());
        log.info("redis地址：{}", host +":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        return new JedisPool(jedisPoolConfig, host, port, timeout);
    }

}
