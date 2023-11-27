package com.example.util;

import com.example.redisson.LockType;
import com.example.servicelock.ServiceLockInfo;
import com.example.servicelock.ServiceLocker;
import com.example.servicelock.info.LockTimeOutStrategy;
import com.example.redisson.factory.ServiceLockFactory;
import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-example
 * @description: 提供方法级别的分布式锁
 * @author: 星哥
 * @create: 2023-05-28
 **/
@AllArgsConstructor
public class ServiceLockUtil {
    
    private final ServiceLockFactory serviceLockFactory;

    private final ServiceLockInfo serviceLockInfo;
    
    public void execute(TaskRun taskRun,String name,String [] keys) {
        execute(taskRun,name,keys,10);
    } 

    /**
     * 没有返回值的加锁执行
     * @param taskRun 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     * @param waitTime 等待时间
     * */
    public void execute(TaskRun taskRun,String name,String [] keys,long waitTime){
        String lockName = serviceLockInfo.simpleGetLockName(name,keys);
        ServiceLocker lock = serviceLockFactory.getLock(LockType.Reentrant);
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
        String lockName = serviceLockInfo.simpleGetLockName(name,keys);
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
}
