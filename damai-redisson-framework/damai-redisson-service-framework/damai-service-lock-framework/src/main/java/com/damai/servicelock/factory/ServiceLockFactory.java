package com.damai.servicelock.factory;

import com.damai.servicelock.LockType;
import com.damai.servicelock.impl.RedissonFairLocker;
import com.damai.servicelock.impl.RedissonReadLocker;
import com.damai.servicelock.impl.RedissonReentrantLocker;
import com.damai.servicelock.impl.RedissonWriteLocker;
import com.damai.servicelock.ServiceLocker;
import lombok.AllArgsConstructor;
import org.redisson.api.RedissonClient;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分布式锁类型工厂
 * @author: 阿宽不是程序员
 **/
@AllArgsConstructor
public class ServiceLockFactory {

    
    private final RedissonClient redissonClient;
    

    public ServiceLocker getLock(LockType lockType){
        ServiceLocker lock;
        switch (lockType) {
            case Fair:
                lock = new RedissonFairLocker(redissonClient);
                break;
            case Write:
                lock = new RedissonWriteLocker(redissonClient);
                break;
            case Read:
                lock = new RedissonReadLocker(redissonClient);
                break;
            default:
                lock = new RedissonReentrantLocker(redissonClient);
                break;
        }
        return lock;
    }
}
