package com.damai.config;


import com.damai.RedisStreamConfigProperties;
import com.damai.RedisStreamGroup;
import com.damai.RedisStreamListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream消费端配置
 * @author: 阿宽不是程序员
 **/

@Configuration
@Slf4j
public class RedisStreamConsumerAutoConfig  {
    
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    
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
            RedisStreamListener streamListener,
            RedisStreamConfigProperties redisStreamConfigProperties) {
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
                        log.error(error.getMessage());
                    })
                    .cancelOnError(e -> false)
                    .consumer(consumer)
                    //关闭自动ack确认
                    .autoAcknowledge(false)
                    .build();
        } catch (Exception e) {
            log.error("streamReadRequest builder error",e);
        }
        //指定消费者对象
        container.register(streamReadRequest, streamListener);
        container.start();
        return container;
    }
    
    @Bean
    public RedisStreamGroup redisStreamGroup(StringRedisTemplate stringRedisTemplate,
                                             RedisStreamConfigProperties redisStreamConfigProperties){
        return new RedisStreamGroup(stringRedisTemplate,redisStreamConfigProperties);
    }
}
