package com.example.distributecache.operate.impl;

import com.example.distributecache.operate.Operate;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: redisson操作接口实现类
 * @author: lk
 * @create: 2022-05-28
 **/
@Component("redissonOperate")
public class RedissonOperate implements Operate {

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void set(String name,Object o) {
        RBucket<Object> bucket = redissonClient.getBucket(name);
        bucket.set(o);
    }

    @Override
    public void set(String name, Object o, long timeToLive, TimeUnit timeUnit) {
        RBucket<Object> bucket = redissonClient.getBucket(name);
        bucket.set(o,timeToLive,timeUnit);
    }

    @Override
    public Object get(String name) {
        RBucket<Object> bucket = redissonClient.getBucket(name);
        return bucket.get();
    }
}
