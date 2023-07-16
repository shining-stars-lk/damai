package com.tool.servicelock.util;

import com.tool.servicelock.ServiceLockInfoProvider;
import com.tool.servicelock.ServiceLocker;
import com.tool.servicelock.info.LockTimeOutStrategy;
import com.tool.servicelock.redisson.LockType;
import com.tool.servicelock.redisson.factory.ServiceLockFactory;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: 提供方法级别的分布式锁
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class ServiceLockUtil {
    
    private ServiceLockFactory serviceLockFactory;

    private ServiceLockInfoProvider distributedLockInfoProvider;
    
    public ServiceLockUtil(ServiceLockFactory serviceLockFactory, ServiceLockInfoProvider distributedLockInfoProvider){
        this.serviceLockFactory = serviceLockFactory;
        this.distributedLockInfoProvider = distributedLockInfoProvider;
    }

    /**
     * 没有返回值的加锁执行
     * @param taskRun 要执行的任务
     * @param name 锁的业务名
     * @param keys 锁的标识
     * */
    public void execute(TaskRun taskRun,String name,String [] keys){
        String lockName = distributedLockInfoProvider.simpleGetLockName(name,keys);
        ServiceLocker lock = serviceLockFactory.createLock(LockType.Reentrant);
        boolean result = lock.tryLock(lockName, TimeUnit.SECONDS, 30);
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
        String lockName = distributedLockInfoProvider.simpleGetLockName(name,keys);
        ServiceLocker lock = serviceLockFactory.createLock(LockType.Reentrant);
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
