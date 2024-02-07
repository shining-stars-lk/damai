package com.example.lockinfo;

/**
 * @program: redis-example
 * @description: 锁信息类型
 * @author: 星哥
 * @create: 2024-01-28
 **/
public enum LockInfoType {
    /**
     * 防重复提交
     */
    REPEAT_EXECUTE_LIMIT,
    /**
     * 分布式锁锁
     */
    SERVICE_LOCK;

    LockInfoType() {
    }

}
