package com.example.config;

import com.example.constant.LockInfoType;
import com.example.lockinfo.LockInfoHandle;
import com.example.lockinfo.factory.LockInfoHandleFactory;
import com.example.lockinfo.impl.ServiceLockInfoHandle;
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
public class ServiceLockAutoConfiguration {
    
    @Bean(LockInfoType.SERVICE_LOCK)
    public LockInfoHandle serviceLockInfoHandle(){
        return new ServiceLockInfoHandle();
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
