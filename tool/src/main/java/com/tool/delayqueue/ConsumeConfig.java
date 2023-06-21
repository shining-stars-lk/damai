package com.tool.delayqueue;

import com.tool.servicelock.redisson.config.RedissonAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-19
 **/
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class ConsumeConfig {
    
    @Bean
    public Consumer consumer(RedissonClient redissonClient){
        return new Consumer(redissonClient);
    }
}
