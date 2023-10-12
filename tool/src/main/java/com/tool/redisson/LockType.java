package com.tool.redisson;

/**
 * @program: redis-tool
 * @description: 分布式锁锁类型
 * @author: 星哥
 * @create: 2023-05-28
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
