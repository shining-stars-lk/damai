package com.example.distributecache.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @program: distribute-cache
 * @description: redis配置
 * @author: lk
 * @create: 2022-05-28
 **/
@Configuration
public class RedisConfig {

    @Bean("myRedisTemplate")
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean("myStringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate msaStringRedisTemplate = new StringRedisTemplate();
        msaStringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        msaStringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return msaStringRedisTemplate;
    }
}
