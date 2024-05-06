package com.damai.service.test2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.connection.stream.StreamInfo.XInfoGroup;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
@Slf4j
public class RedisStreamConsumerConfig {
    
    private final AtomicBoolean isCreated = new AtomicBoolean(false);

    private ExecutorService executorService = Executors.newCachedThreadPool();
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    RedisStreamConfigProperties redisStreamConfigProperties;

    /**
     * 主要做的是将OrderStreamListener监听绑定消费者，用于接收消息
     *
     * @param redisConnectionFactory redis连接工厂
     * @param streamListener 监听器
     * @return StreamMessageListenerContainer
     */
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            RedisStreamListener streamListener) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 拉取消息超时时间
                        .pollTimeout(Duration.ofSeconds(5))
                        // 批量抓取消息
                        .batchSize(10)
                        // 传递的数据类型
                        .targetType(String.class) 
                        .executor(executorService)
                        .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                .create(redisConnectionFactory, options);
        //指定消费最新的消息
        StreamOffset<String> offset = StreamOffset.create(redisStreamConfigProperties.getStreamName(), ReadOffset.lastConsumed());
        //创建消费者
        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = null;
        try {
            Consumer consumer = Consumer.from(redisStreamConfigProperties.getConsumerGroup(), redisStreamConfigProperties.getConsumerName());
            streamReadRequest = StreamMessageListenerContainer.StreamReadRequest.builder(offset)
                    .errorHandler((error) -> {
                        error.printStackTrace();
                        log.error(error.getMessage());
                    })
                    .cancelOnError(e -> false)
                    .consumer(consumer)
                    //关闭自动ack确认
                    .autoAcknowledge(false)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //指定消费者对象
        container.register(streamReadRequest, streamListener);
        container.start();
        return container;
    }
    
    @PostConstruct
    public void groupInfo() {
        
        // 发送个心跳
        HashMap<Object, Object> map = new HashMap<>();
        map.put("test","testInfo");
        MapRecord<String, Object, Object> record = StreamRecords.newRecord()
                .in(redisStreamConfigProperties.getStreamName())
                .ofMap(map)
                .withId(RecordId.autoGenerate());
        
        StreamOperations<String, Object, Object> stream = stringRedisTemplate.opsForStream();
        stream.add(record);
        
        StreamInfo.XInfoGroups xInfoGroups = stream.groups(redisStreamConfigProperties.getStreamName());
        
        Collection<XInfoGroup> needDestroyColl = new ArrayList<>();
        
        xInfoGroups.forEach(xInfoStream -> {
            if (xInfoStream.groupName().equals(redisStreamConfigProperties.getConsumerGroup())) {
                isCreated.set(true);
            } else {
                needDestroyColl.add(xInfoStream);
            }
        });
        
        for (XInfoGroup xInfoGroup : needDestroyColl) {
            stream.destroyGroup(redisStreamConfigProperties.getStreamName(),xInfoGroup.groupName());
        }
        
        if (isCreated.get()) {
            return;
        }
        
        log.info("create consumer group : {}", redisStreamConfigProperties.getConsumerGroup());
        
        stream.createGroup(redisStreamConfigProperties.getStreamName(), redisStreamConfigProperties.getConsumerGroup());
        
    }
}
