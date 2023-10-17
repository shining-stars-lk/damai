package com.example.redisson.factory;

import com.example.redisson.LockType;
import com.example.servicelock.ServiceLocker;
import com.example.redisson.impl.RedissonFairLocker;
import com.example.redisson.impl.RedissonReentrantLocker;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;

import java.util.Objects;

/**
 * @program: redis-example
 * @description: 分布式锁类型提供工厂
 * @author: 星哥
 * @create: 2023-05-28
 **/
@AllArgsConstructor
public class ServiceLockFactory {

    
    private final RedissonClient redissonClient;
    

    public ServiceLocker getLock(LockType lockType){
        if (Objects.requireNonNull(lockType) == LockType.Fair) {
            return new RedissonFairLocker(redissonClient);
        }
        return new RedissonReentrantLocker(redissonClient);
    }
}
