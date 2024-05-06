package com.damai.service.test2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DcirStreamListener implements StreamListener<String, ObjectRecord<String, String>> {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisStreamConfig redisStreamConfig;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        try{
            // 消息ID
            RecordId messageId = message.getId();

            // 消息的key和value
            String string = message.getValue();
            log.info("dcir获取到数据。messageId={}, stream={}, body={}", messageId, message.getStream(), string);
            // 通过RedisTemplate手动确认消息 
            this.stringRedisTemplate.opsForStream().acknowledge(redisStreamConfig.getDcirgroup(), message);
        }catch (Exception e){
            // 处理异常
            e.printStackTrace();
        }
    }
}
