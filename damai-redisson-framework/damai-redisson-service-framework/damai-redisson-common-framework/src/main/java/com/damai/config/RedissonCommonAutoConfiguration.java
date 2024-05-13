package com.damai.config;

import com.damai.handle.RedissonDataHandle;
import com.damai.locallock.LocalLockCache;
import com.damai.lockinfo.factory.LockInfoHandleFactory;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redisson通用配置
 * @author: 阿星不是程序员
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
