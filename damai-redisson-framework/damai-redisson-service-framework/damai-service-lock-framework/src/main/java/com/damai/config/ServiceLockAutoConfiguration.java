package com.damai.config;

import com.damai.constant.LockInfoType;
import com.damai.lockinfo.LockInfoHandle;
import com.damai.lockinfo.factory.LockInfoHandleFactory;
import com.damai.lockinfo.impl.ServiceLockInfoHandle;
import com.damai.servicelock.ServiceLocker;
import com.damai.servicelock.aspect.ServiceLockAspect;
import com.damai.servicelock.factory.ServiceLockFactory;
import com.damai.servicelock.impl.RedissonFairLocker;
import com.damai.servicelock.impl.RedissonReadLocker;
import com.damai.servicelock.impl.RedissonReentrantLocker;
import com.damai.servicelock.impl.RedissonWriteLocker;
import com.damai.util.ServiceLockTool;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分布式锁 配置
 * @author: 阿星不是程序员
 **/
public class ServiceLockAutoConfiguration {
    
    @Bean(LockInfoType.SERVICE_LOCK)
    public LockInfoHandle serviceLockInfoHandle(){
        return new ServiceLockInfoHandle();
    }
    
    @Bean
    public ServiceLocker redissonFairLocker(RedissonClient redissonClient){
        return new RedissonFairLocker(redissonClient);
    }
    
    @Bean
    public ServiceLocker redissonWriteLocker(RedissonClient redissonClient){
        return new RedissonWriteLocker(redissonClient);
    }

    @Bean
    public ServiceLocker redissonReadLocker(RedissonClient redissonClient){
        return new RedissonReadLocker(redissonClient);
    }

    @Bean
    public ServiceLocker redissonReentrantLocker(RedissonClient redissonClient){
        return new RedissonReentrantLocker(redissonClient);
    }
   
    @Bean
    public ServiceLockFactory serviceLockFactory(ServiceLocker redissonFairLocker,
                                                 ServiceLocker redissonWriteLocker,
                                                 ServiceLocker redissonReadLocker,
                                                 ServiceLocker redissonReentrantLocker){
        return new ServiceLockFactory(redissonFairLocker,redissonWriteLocker,redissonReadLocker,redissonReentrantLocker);
    }
    
    @Bean
    public ServiceLockAspect serviceLockAspect(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
        return new ServiceLockAspect(lockInfoHandleFactory,serviceLockFactory);
    }
    
    @Bean
    public ServiceLockTool serviceLockUtil(LockInfoHandleFactory lockInfoHandleFactory,ServiceLockFactory serviceLockFactory){
        return new ServiceLockTool(lockInfoHandleFactory,serviceLockFactory);
    }
}
