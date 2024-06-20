package com.damai.servicelock;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分布式锁 方法抽象
 * @author: 阿星不是程序员
 **/
public interface ServiceLocker {
    
    /**
     * 获取锁
     * @param lockKey 锁的key
     * @return 结果
     * */
    RLock getLock(String lockKey);
    
    /**
     * 加锁
     * @param lockKey 锁的key
     * @return 结果
     * */
    RLock lock(String lockKey);
    
    /**
     * 加锁
     * @param lockKey 锁的key
     * @param leaseTime 释放时间
     * @return 结果
     * */
    RLock lock(String lockKey, long leaseTime);
    
    /**
     * 加锁
     * @param lockKey 锁的key
     * @param unit 时间单位
     * @param leaseTime 释放时间
     * @return 结果
     * */
    RLock lock(String lockKey, TimeUnit unit, long leaseTime);
    
    /**
     * 加锁
     * @param lockKey 锁的key
     * @param unit 时间单位
     * @param waitTime 等待时间
     * @return 结果
     * */
    boolean tryLock(String lockKey, TimeUnit unit, long waitTime);
    
    /**
     * 加锁
     * @param lockKey 锁的key
     * @param unit 时间单位
     * @param waitTime 等待时间
     * @param leaseTime 释放时间
     * @return 结果
     * */
    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);
    
    /**
     * 解锁
     * @param lockKey 锁的key
     * */
    void unlock(String lockKey);
    
    /**
     * 解锁
     * @param lock 锁
     * */
    void unlock(RLock lock);
}