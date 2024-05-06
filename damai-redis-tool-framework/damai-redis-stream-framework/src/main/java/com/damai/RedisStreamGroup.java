package com.damai;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream消费组
 * @author: 阿宽不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class RedisStreamGroup implements InitializingBean {
    
    
    private final StringRedisTemplate stringRedisTemplate;
    
    
    private final RedisStreamConfigProperties redisStreamConfigProperties;
    
    private final AtomicBoolean isCreated = new AtomicBoolean(false);
    
    @Override
    public void afterPropertiesSet() {
        //这里要先创建队列和分组，否则会报错 ERR no such key
        HashMap<Object, Object> map = new HashMap<>(4);
        map.put("test","testInfo");
        MapRecord<String, Object, Object> record = StreamRecords.newRecord()
                .in(redisStreamConfigProperties.getStreamName())
                .ofMap(map)
                .withId(RecordId.autoGenerate());
        
        StreamOperations<String, Object, Object> stream = stringRedisTemplate.opsForStream();
        stream.add(record);
        
        StreamInfo.XInfoGroups xInfoGroups = stream.groups(redisStreamConfigProperties.getStreamName());
        
        xInfoGroups.forEach(xInfoStream -> {
            if (xInfoStream.groupName().equals(redisStreamConfigProperties.getConsumerGroup())) {
                isCreated.set(true);
            }
        });
        
        if (isCreated.get()) {
            return;
        }
        
        log.info("create consumer group : {}", redisStreamConfigProperties.getConsumerGroup());
        
        stream.createGroup(redisStreamConfigProperties.getStreamName(), redisStreamConfigProperties.getConsumerGroup());
        
    }
}
