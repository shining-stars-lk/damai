package com.tool.operate.impl;

import com.tool.operate.Operate;
import lombok.AllArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: redisson操作接口实现类
 * @author: 星哥
 * @create: 2023-05-28
 **/
@AllArgsConstructor
public class RedissonOperate implements Operate {
    
    private final RedissonClient redissonClient;

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
