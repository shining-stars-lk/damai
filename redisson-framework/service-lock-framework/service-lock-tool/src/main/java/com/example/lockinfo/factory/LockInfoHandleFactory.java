package com.example.lockinfo.factory;

import com.example.lockinfo.LockInfoHandle;
import com.example.lockinfo.LockInfoType;
import com.example.lockinfo.impl.RepeatExecuteLimitLockInfoHandle;
import com.example.lockinfo.impl.ServiceLockInfoHandle;

import java.util.Objects;

/**
 * @program: redis-example
 * @description: 锁信息提供工厂
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class LockInfoHandleFactory {

    public LockInfoHandle getLockInfoHandle(LockInfoType lockInfoType){
        LockInfoHandle lockInfoHandle;
        if (Objects.requireNonNull(lockInfoType) == LockInfoType.REPEAT_EXECUTE_LIMIT) {
            lockInfoHandle = new RepeatExecuteLimitLockInfoHandle();
        } else {
            lockInfoHandle = new ServiceLockInfoHandle();
        }
        return lockInfoHandle;
    }
}
