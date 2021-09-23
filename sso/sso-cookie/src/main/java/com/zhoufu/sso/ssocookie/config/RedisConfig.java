package com.zhoufu.sso.ssocookie.config;

import com.zhoufu.sso.ssocookie.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author: zhoufu
 * @Date: 2021/2/22 11:47
 * @description: Redis配置
 */
@Configuration
public class RedisConfig {
    /**
     * 配置Redis的序列化，  用户entity中User 的序列化
     */
    @Bean
    public RedisTemplate<String, User> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, User> template = new RedisTemplate<>();
        // 关联
        template.setConnectionFactory(factory);
        // 设置key 的序列化
        template.setKeySerializer(new StringRedisSerializer());
        // 设置value的序列化 器为json
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        return template;
    }
}
