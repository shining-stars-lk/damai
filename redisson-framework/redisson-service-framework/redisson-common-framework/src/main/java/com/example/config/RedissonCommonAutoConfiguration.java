package com.example.config;

import com.example.handle.RedissonDataHandle;
import com.example.locallock.LocalLockCache;
import com.example.lockinfo.factory.LockInfoHandleFactory;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-02-23
 **/

public class RedissonCommonAutoConfiguration {
    
    @Bean
    public RedissonDataHandle redissonDataHandle(RedissonClient redissonClient){
        return new RedissonDataHandle(redissonClient);
    }
    
    @Bean
    public LocalLockCache localLockCache(){
        return new LocalLockCache();
    }
    
    @Bean
    public LockInfoHandleFactory lockInfoHandleFactory(){
        return new LockInfoHandleFactory();
    }
}
