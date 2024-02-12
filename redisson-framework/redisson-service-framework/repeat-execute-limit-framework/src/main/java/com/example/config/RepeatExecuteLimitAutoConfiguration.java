package com.example.config;

import com.example.constant.LockInfoType;
import com.example.handle.RedissonDataHandle;
import com.example.locallock.LocalLockCache;
import com.example.lockinfo.LockInfoHandle;
import com.example.lockinfo.factory.LockInfoHandleFactory;
import com.example.lockinfo.impl.RepeatExecuteLimitLockInfoHandle;
import com.example.repeatexecutelimit.aspect.RepeatExecuteLimitAspect;
import com.example.servicelock.factory.ServiceLockFactory;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-02-23
 **/
public class RepeatExecuteLimitAutoConfiguration {
    
    @Bean(LockInfoType.REPEAT_EXECUTE_LIMIT)
    public LockInfoHandle serviceLockInfoHandle(){
        return new RepeatExecuteLimitLockInfoHandle();
    }
    
    @Bean
    public RepeatExecuteLimitAspect repeatExecuteLimitAspect(LocalLockCache localLockCache,LockInfoHandleFactory lockInfoHandleFactory,
                                                             ServiceLockFactory serviceLockFactory,RedissonDataHandle redissonDataHandle){
        return new RepeatExecuteLimitAspect(localLockCache, lockInfoHandleFactory,serviceLockFactory,redissonDataHandle);
    }
}
