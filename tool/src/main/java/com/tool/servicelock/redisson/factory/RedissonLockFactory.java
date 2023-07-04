package com.tool.servicelock.redisson.factory;

import com.tool.servicelock.ServiceLocker;
import com.tool.servicelock.redisson.LockType;
import com.tool.servicelock.redisson.impl.RedissonFairLocker;
import com.tool.servicelock.redisson.impl.RedissonReentrantLocker;
import org.redisson.api.RedissonClient;

/**
 * @program: redis-tool
 * @description: 分布式锁类型提供工厂
 * @author: k
 * @create: 2022-05-28
 **/
public class RedissonLockFactory {

    
    private RedissonClient redissonClient;
    
    public RedissonLockFactory(RedissonClient redissonClient){
        this.redissonClient = redissonClient;
    }

    public ServiceLocker createLock(LockType lockType){
        switch(lockType) {
            case Reentrant:
                return new RedissonReentrantLocker(redissonClient);
            case Fair:
                return new RedissonFairLocker(redissonClient);
            default:
                return new RedissonReentrantLocker(redissonClient);
        }
    }
}
