package com.example.distributecache.distributedlock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 分布式锁接口
 * @author: lk
 * @create: 2022-05-28
 **/
public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, long leaseTime);

    RLock lock(String lockKey, TimeUnit unit, long leaseTime);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime);

    void unlock(String lockKey);

    void unlock(RLock lock);
}