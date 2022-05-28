package com.example.distributecache.distributedlock.redisson.factory;

import com.example.distributecache.distributedlock.DistributedLocker;
import com.example.distributecache.distributedlock.redisson.LockType;
import com.example.distributecache.distributedlock.redisson.impl.RedissonFairLocker;
import com.example.distributecache.distributedlock.redisson.impl.RedissonReentrantLocker;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: distribute-cache
 * @description: 分布式锁类型提供工厂
 * @author: lk
 * @create: 2022-05-28
 **/
@Component
public class RedissonLockFactory {

    @Autowired
    private RedissonClient redissonClient;

    public DistributedLocker createLock(LockType lockType){
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
