package com.example.distributedlock.util;

import com.example.distributedlock.DistributedLockInfoProvider;
import com.example.distributedlock.DistributedLocker;
import com.example.distributedlock.info.LockTimeOutStrategy;
import com.example.distributedlock.redisson.LockType;
import com.example.distributedlock.redisson.factory.RedissonLockFactory;

import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 提供方法级别的分布式锁
 * @author: lk
 * @create: 2022-05-28
 **/
public class DistributedLockUtil {
    
    private RedissonLockFactory redissonLockFactory;

    private DistributedLockInfoProvider distributedLockInfoProvider;
    
    public DistributedLockUtil(RedissonLockFactory redissonLockFactory, DistributedLockInfoProvider distributedLockInfoProvider){
        this.redissonLockFactory = redissonLockFactory;
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
        DistributedLocker lock = redissonLockFactory.createLock(LockType.Reentrant);
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
        DistributedLocker lock = redissonLockFactory.createLock(LockType.Reentrant);
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
