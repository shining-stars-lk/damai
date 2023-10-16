package com.tool.redisson.factory;

import com.tool.redisson.LockType;
import com.tool.servicelock.ServiceLocker;
import com.tool.redisson.impl.RedissonFairLocker;
import com.tool.redisson.impl.RedissonReentrantLocker;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;

import java.util.Objects;

/**
 * @program: redis-tool
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
