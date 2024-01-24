package com.example.delayconsumer;

import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-19
 **/
public class ConsumeConfig {
    
    @Bean
    public Consumer consumer(RedissonClient redissonClient){
        return new Consumer(redissonClient);
    }
}
