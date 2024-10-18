package com.damai.service;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-10-10
 **/
@Service
public class TestService {

    @Autowired
    RedissonClient redissonClient;
    
    public void testRedissonLock(){
        RLock lock = redissonClient.getLock("testLock");
        lock.lock();
        try {
            lock.lock();
            //执行业务逻辑....
            System.out.println("执行业务逻辑");
        }finally {
            lock.unlock();
        }
    }
}
