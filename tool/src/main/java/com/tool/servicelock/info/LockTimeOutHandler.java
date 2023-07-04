package com.tool.servicelock.info;

/**
 * @program: redis-tool
 * @description: 分布式锁失败处理接口
 * @author: k
 * @create: 2022-05-28
 **/
public interface LockTimeOutHandler {

    void handler(String lockName);
}
