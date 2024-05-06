package com.damai.service.test2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Slf4j
public class RedisStreamConsumerConfig {


    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    RedisStreamConfig redisStreamConfig;

    /**
     * 主要做的是将OrderStreamListener监听绑定消费者，用于接收消息
     *
     * @param connectionFactory
     * @param streamListener
     * @return
     */
    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> dcirConsumerListener(
            RedisConnectionFactory connectionFactory,
            DcirStreamListener streamListener) {
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
                streamContainer(RedisStreamConfig.DCIR, connectionFactory, streamListener);
        container.start();
        return container;
    }

    /**
     * @param mystream          从哪个流接收数据
     * @param connectionFactory
     * @param streamListener    绑定的监听类
     * @return
     */
    private StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamContainer(String mystream, RedisConnectionFactory connectionFactory, StreamListener<String, ObjectRecord<String, String>> streamListener) {
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(5)) // 拉取消息超时时间
                        .batchSize(10) // 批量抓取消息
                        .targetType(String.class) // 传递的数据类型
                        .executor(executorService)
                        .build();
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = StreamMessageListenerContainer
                .create(connectionFactory, options);
        //指定消费最新的消息
        StreamOffset<String> offset = StreamOffset.create(mystream, ReadOffset.lastConsumed());
        //创建消费者
        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = null;
        try {
            streamReadRequest = buildStreamReadRequest(offset, streamListener);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        //指定消费者对象
        container.register(streamReadRequest, streamListener);
        return container;
    }

    private StreamMessageListenerContainer.StreamReadRequest<String> buildStreamReadRequest(StreamOffset<String> offset, StreamListener<String, ObjectRecord<String, String>> streamListener) throws Exception {
        Consumer consumer = null;
        if(streamListener instanceof DcirStreamListener){
            consumer = Consumer.from(redisStreamConfig.getDcirgroup(), redisStreamConfig.getDcirconsumer());
        }else{
            throw new Exception("无法识别的stream key");
        }
        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = StreamMessageListenerContainer.StreamReadRequest.builder(offset)
                .errorHandler((error) -> {
                    error.printStackTrace();
                    log.error(error.getMessage());
                })
                .cancelOnError(e -> false)
                .consumer(consumer)
                //关闭自动ack确认
                .autoAcknowledge(false)
                .build();
        return streamReadRequest;
    }

//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(factory);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        // key采用String的序列化方式
//        template.setKeySerializer(stringRedisSerializer);
//        // hash的key也采用String的序列化方式
//        template.setHashKeySerializer(stringRedisSerializer);
//        // value序列化方式采用jackson
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        // hash的value序列化方式采用jackson
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }

}
