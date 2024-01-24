package com.example.delayqueue;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-19
 **/
public class ProduceConfig {

    @Bean
    public Producer producer(RedissonClient redissonClient){
        return new Producer(redissonClient);
    }
    
}
