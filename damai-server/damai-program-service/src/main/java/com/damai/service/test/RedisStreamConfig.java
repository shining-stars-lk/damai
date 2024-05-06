//package com.damai.service.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.connection.stream.ReadOffset;
//import org.springframework.data.redis.connection.stream.StreamOffset;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//
//import java.time.Duration;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Slf4j
//@Configuration  
//public class RedisStreamConfig {
//    
//    private ExecutorService executorService = Executors.newCachedThreadPool();
//
//    @Bean  
//    StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamListenerContainer(RedisConnectionFactory redisConnectionFactory,  
//                                                           MyStreamMessageListener listener) {
//        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>>
//                options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
//                .builder()
//                // 一次最多获取多少条消息
//                .batchSize(5)
//                // 	执行消息轮询的执行器
//                .executor(executorService)
//                // 超时时间，设置为0，表示不超时（超时后会抛出异常）
//                .pollTimeout(Duration.ZERO)
//                // 消息消费异常的handler
//                .errorHandler(e-> log.error("发生了异常", e))
//                // 序列化器 或者RedisSerializer.string()
//                .serializer(new StringRedisSerializer())
//                .build();
//
//        StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer = StreamMessageListenerContainer
//                .create(redisConnectionFactory, options);
//
//        // receiveAutoAck(自动ack)
//        streamMessageListenerContainer.receive(
//                StreamOffset.create(StreamConstant.streamName, ReadOffset.lastConsumed()),
//                listener
//        );
//
//        return streamMessageListenerContainer;
//    }  
//}