package com.damai.config;

import com.damai.MessageConsumer;
import com.damai.RedisStreamConfigProperties;
import com.damai.RedisStreamHandler;
import com.damai.RedisStreamListener;
import com.damai.RedisStreamPushHandler;
import com.damai.constant.RedisStreamConstant;
import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream配置
 * @author: 阿宽不是程序员
 **/
@Slf4j
@EnableConfigurationProperties(RedisStreamConfigProperties.class)
public class RedisStreamAutoConfig {
    
    @Bean
    public RedisStreamPushHandler redisStreamPushHandler(StringRedisTemplate stringRedisTemplate, 
                                                         RedisStreamConfigProperties redisStreamConfigProperties) {
        return new RedisStreamPushHandler(stringRedisTemplate, redisStreamConfigProperties);
    }
    
    @Bean
    public RedisStreamHandler redisStreamHandler(RedisStreamPushHandler redisStreamPushHandler, 
                                                 StringRedisTemplate stringRedisTemplate) {
        return new RedisStreamHandler(redisStreamPushHandler, stringRedisTemplate);
    }
    
    /**
     * 主要做的是将OrderStreamListener监听绑定消费者，用于接收消息
     *
     * @param redisConnectionFactory redis连接工厂
     * @return StreamMessageListenerContainer
     */
    @Bean
    @ConditionalOnBean(MessageConsumer.class)
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory, 
            RedisStreamConfigProperties redisStreamConfigProperties, 
            RedisStreamHandler redisStreamHandler, 
            MessageConsumer messageConsumer) {
        //消息侦听容器，创建后，StreamMessageListenerContainer可以订阅Redis流并使用传入的消息
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> 
                options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                    //拉取消息超时时间
                    .pollTimeout(Duration.ofSeconds(5))
                    //批量抓取消息的数量
                    .batchSize(10)
                    //传递的数据类型
                    .targetType(String.class)
                    //获取消息的过程或获取到消息给具体的消息者处理的过程中，发生了异常的处理
                    .errorHandler(t -> log.error("出现异常", t))
                    //执行拉取任务的执行线程池
                    .executor(createThreadPool()).build();
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> container = 
                StreamMessageListenerContainer.create(redisConnectionFactory, options);
        //检查消费类型，消费组或者广播
        checkConsumerType(redisStreamConfigProperties.getConsumerType());
        //监听器
        RedisStreamListener redisStreamListener = new RedisStreamListener(messageConsumer);
        //如果是分组消费
        if (RedisStreamConstant.GROUP.equals(redisStreamConfigProperties.getConsumerType())) {
            //绑定stream和消费组
            redisStreamHandler.streamBindingGroup(redisStreamConfigProperties.getStreamName(), 
                    redisStreamConfigProperties.getConsumerGroup());
            //这里用的是ack自动提交
            container.receiveAutoAck(Consumer.from(redisStreamConfigProperties.getConsumerGroup(), 
                    redisStreamConfigProperties.getConsumerName()), 
                    StreamOffset.create(redisStreamConfigProperties.getStreamName(), ReadOffset.lastConsumed()), 
                    redisStreamListener);
        } else {
            //如果是广播消费
            container.receive(StreamOffset.fromStart(redisStreamConfigProperties.getStreamName()), redisStreamListener);
        }
        //启动监听
        container.start();
        return container;
    }
    
    public ThreadPoolExecutor createThreadPool(){
        //线程池
        int coreThreadCount = Runtime.getRuntime().availableProcessors();
        AtomicInteger threadCount = new AtomicInteger(1);
        return new ThreadPoolExecutor(
                coreThreadCount,
                2 * coreThreadCount,
                30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(100),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread-consumer-stream-task-" + threadCount.getAndIncrement());
                    return thread;
                });
    }
    
    public void checkConsumerType(String consumerType){
        if ((!RedisStreamConstant.GROUP.equals(consumerType)) && (!RedisStreamConstant.BROADCAST.equals(consumerType))) {
            throw new DaMaiFrameException(BaseCode.REDIS_STREAM_CONSUMER_TYPE_NOT_EXIST);
        }
    }
}
