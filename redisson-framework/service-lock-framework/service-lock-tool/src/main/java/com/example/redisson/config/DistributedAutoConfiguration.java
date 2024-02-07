package com.example.redisson.config;

import com.example.handle.RedissonDataHandle;
import com.example.locallock.LocalLockCache;
import com.example.lockinfo.factory.LockInfoHandleFactory;
import com.example.redisson.RedissonProperties;
import com.example.repeatexecutelimit.aspect.RepeatExecuteLimitAspect;
import com.example.servicelock.aspect.ServiceLockAspect;
import com.example.servicelock.factory.ServiceLockFactory;
import com.example.util.RBloomFilterUtil;
import com.example.util.ServiceLockTool;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-02-23
 **/
@EnableConfigurationProperties(RedissonProperties.class)
public class DistributedAutoConfiguration {
    
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
    
    @Bean
    public ServiceLockFactory serviceLockFactory(RedissonClient redissonClient){
        return new ServiceLockFactory(redissonClient);
    }
    
    @Bean
    public ServiceLockAspect serviceLockAspect(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
        return new ServiceLockAspect(lockInfoHandleFactory,serviceLockFactory);
    }
    
    @Bean
    public RepeatExecuteLimitAspect repeatExecuteLimitAspect(LocalLockCache localLockCache,LockInfoHandleFactory lockInfoHandleFactory,
                                                             ServiceLockFactory serviceLockFactory,RedissonDataHandle redissonDataHandle){
        return new RepeatExecuteLimitAspect(localLockCache, lockInfoHandleFactory,serviceLockFactory,redissonDataHandle);
    }
    
    @Bean
    public ServiceLockTool serviceLockUtil(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
        return new ServiceLockTool(lockInfoHandleFactory,serviceLockFactory);
    }
    
    /**
     * 布隆过滤器
     */
    @Bean
    public RBloomFilterUtil rBloomFilterUtil(RedissonClient redissonClient, RedissonProperties redissonProperties) {
        return new RBloomFilterUtil(redissonClient,redissonProperties);
    }
}
