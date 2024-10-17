package com.damai.servicelock.factory;

import com.damai.servicelock.LockType;
import com.damai.servicelock.ServiceLocker;
import lombok.AllArgsConstructor;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分布式锁类型工厂
 * @author: 阿星不是程序员
 **/
@AllArgsConstructor
public class ServiceLockFactory {
    
    private final ServiceLocker redissonFairLocker;
    
    private final ServiceLocker redissonWriteLocker;
    
    private final ServiceLocker redissonReadLocker;
    
    private final ServiceLocker redissonReentrantLocker;
    

    public ServiceLocker getLock(LockType lockType){
        ServiceLocker lock;
        switch (lockType) {
            case Fair:
                lock = redissonFairLocker;
                break;
            case Write:
                lock = redissonWriteLocker;
                break;
            case Read:
                lock = redissonReadLocker;
                break;
            default:
                lock = redissonReentrantLocker;
                break;
        }
        return lock;
    }
}
