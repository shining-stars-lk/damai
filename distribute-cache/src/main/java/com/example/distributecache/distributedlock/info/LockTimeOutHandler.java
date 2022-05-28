package com.example.distributecache.distributedlock.info;

/**
 * @program: distribute-cache
 * @description: 分布式锁失败处理接口
 * @author: lk
 * @create: 2022-05-28
 **/
public interface LockTimeOutHandler {

    void handler(String lockName);
}
