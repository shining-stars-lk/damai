package com.example.util;


import com.example.constant.LockInfoType;
import com.example.lockinfo.LockInfoHandle;
import com.example.lockinfo.factory.LockInfoHandleFactory;
import com.example.servicelock.LockType;
import com.example.servicelock.ServiceLocker;
import com.example.servicelock.factory.ServiceLockFactory;
import com.example.servicelock.info.LockTimeOutStrategy;
import lombok.AllArgsConstructor;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-example
 * @description: 提供方法级别的分布式锁
 * @author: 星哥
 * @create: 2023-05-28
 **/
@AllArgsConstructor
public class ServiceLockTool {
    
    private final LockInfoHandleFactory lockInfoHandleFactory;
    
    private final ServiceLockFactory serviceLockFactory;
    
    /**
     * 没有返回值的加锁执行
     * @param taskRun 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     *
     * */
    public void execute(TaskRun taskRun,String name,String [] keys) {
        execute(taskRun,name,keys,20);
    } 

    /**
     * 没有返回值的加锁执行
     * @param taskRun 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     * @param waitTime 等待时间
     * 
     * */
    public void execute(TaskRun taskRun,String name,String [] keys,long waitTime){
        execute(LockType.Reentrant,taskRun,name,keys,waitTime);
    }
    
    /**
     * 没有返回值的加锁执行
     * @param lockType 锁类型
     * @param taskRun 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     *
     * */
    public void execute(LockType lockType,TaskRun taskRun,String name,String [] keys) {
        execute(lockType,taskRun,name,keys,20);
    }
    
    /**
     * 没有返回值的加锁执行
     * @param lockType 锁类型
     * @param taskRun 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     * @param waitTime 等待时间
     *
     * */
    public void execute(LockType lockType,TaskRun taskRun,String name,String [] keys,long waitTime) {
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle(LockInfoType.SERVICE_LOCK);
        String lockName = lockInfoHandle.simpleGetLockName(name,keys);
        ServiceLocker lock = serviceLockFactory.getLock(lockType);
        boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, waitTime);
        if (result) {
            try {
                taskRun.run();
            }finally {
                lock.unlock(lockName);
            }
        }else {
            LockTimeOutStrategy.FAIL.handler(lockName);
        }
    }

    /**
     * 有返回值的加锁执行
     * @param taskCall 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     * @return 要执行的任务的返回值
     * */
    public <T> T submit(TaskCall<T> taskCall,String name,String [] keys){
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle(LockInfoType.SERVICE_LOCK);
        String lockName = lockInfoHandle.simpleGetLockName(name,keys);
        ServiceLocker lock = serviceLockFactory.getLock(LockType.Reentrant);
        boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, 30);
        if (result) {
            try {
                return taskCall.call();
            }finally {
                lock.unlock(lockName);
            }
        }else {
            LockTimeOutStrategy.FAIL.handler(lockName);
        }
        return null;
    }
    
    /**
     * 获得锁
     * @param lockType 锁类型
     * @param name 锁的业务名
     * @param keys 锁的标识
     *
     * */
    public RLock getLock(LockType lockType, String name, String [] keys) {
        LockInfoHandle lockInfoHandle = lockInfoHandleFactory.getLockInfoHandle(LockInfoType.SERVICE_LOCK);
        String lockName = lockInfoHandle.simpleGetLockName(name,keys);
        ServiceLocker lock = serviceLockFactory.getLock(lockType);
        return lock.getLock(lockName);
    }
    
    /**
     * 获得锁
     * @param lockType 锁类型
     * @param lockName 锁名
     *
     * */
    public RLock getLock(LockType lockType, String lockName) {
        ServiceLocker lock = serviceLockFactory.getLock(lockType);
        return lock.getLock(lockName);
    }
}
