package com.damai;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream发送消息
 * @author: 阿宽不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class RedisStreamPushHandler {

    
    private final StringRedisTemplate stringRedisTemplate;
    
    private final RedisStreamConfigProperties redisStreamConfigProperties;

    public RecordId push(String msg){
        // 创建消息记录, 以及指定stream
        ObjectRecord<String, String> record = StreamRecords.newRecord()
                //streamName
                .in(redisStreamConfigProperties.getStreamName())
                //消息数据
                .ofObject(msg) 
                .withId(RecordId.autoGenerate());
        // 将消息添加至消息队列中
        RecordId recordId = this.stringRedisTemplate.opsForStream().add(record);
        log.info("redis streamName : {} message : {}", redisStreamConfigProperties.getStreamName(),msg);
        return recordId;
    }
}
