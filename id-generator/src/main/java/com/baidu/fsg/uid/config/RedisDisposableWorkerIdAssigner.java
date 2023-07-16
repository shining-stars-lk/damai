package com.baidu.fsg.uid.config;

import com.baidu.fsg.uid.worker.WorkerIdAssigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-05-23
 **/
public class RedisDisposableWorkerIdAssigner implements WorkerIdAssigner {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDisposableWorkerIdAssigner.class);
    
    private RedisTemplate redisTemplate;
    
    public RedisDisposableWorkerIdAssigner (RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    
    @Override
    public long assignWorkerId() {
        String key = "uid_work_id";
        Long increment = redisTemplate.opsForValue().increment(key);
        return increment;
    }
}
