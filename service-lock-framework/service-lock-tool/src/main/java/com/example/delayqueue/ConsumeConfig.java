package com.example.delayqueue;

import com.example.redisson.config.RedissonAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-19
 **/
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class ConsumeConfig {
    
    @Bean
    public Consumer consumer(RedissonClient redissonClient){
        return new Consumer(redissonClient);
    }
}
