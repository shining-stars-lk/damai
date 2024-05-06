package com.damai.service.test2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class RedisPushService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisStreamConfig redisStreamConfig;

    public void push(String msg){
        // 创建消息记录, 以及指定stream
        StringRecord stringRecord = StreamRecords.string(Collections.singletonMap("data", msg)).withStreamKey(RedisStreamConfig.DCIR);
        // 将消息添加至消息队列中
        this.stringRedisTemplate.opsForStream().add(stringRecord);
        log.info("{}已发送消息:{}",RedisStreamConfig.DCIR,msg);
    }
}
