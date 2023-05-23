package com.tool.servicelock.redisson;

/**
 * @program: distribute-cache
 * @description: 分布式锁锁类型
 * @author: lk
 * @create: 2022-05-28
 **/
public enum LockType {
    /**
     * 可重入锁
     */
    Reentrant,
    /**
     * 公平锁
     */
    Fair;

    LockType() {
    }

}