package com.tool.servicelock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 分布式锁接口
 * @author: k
 * @create: 2022-05-28
 **/
public interface ServiceLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long leaseTime);

    RLock lock(String lockKey, TimeUnit unit, long leaseTime);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime);

    void unlock(String lockKey);

    void unlock(RLock lock);
}