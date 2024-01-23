package com.example.delayqueuenew.config;


import com.example.delayqueuenew.context.DelayQueueContext;
import com.example.delayqueuenew.event.DelayQueueInitHandler;
import com.example.redisson.RedissonProperties;
import com.example.redisson.config.DistributedAutoConfiguration;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-23
 **/
@AutoConfigureAfter(DistributedAutoConfiguration.class)
public class DelayQueueAutoConfig {
    
    @Bean
    public DelayQueueInitHandler DelayQueueInitHandler(RedissonProperties redissonProperties, RedissonClient redissonClient,
                                                       DelayQueueContext delayQueueContext){
        return new DelayQueueInitHandler(redissonProperties, redissonClient, delayQueueContext);
    }
    
    @Bean
    public DelayQueueContext delayQueueContext(){
        return new DelayQueueContext();
    }
}
