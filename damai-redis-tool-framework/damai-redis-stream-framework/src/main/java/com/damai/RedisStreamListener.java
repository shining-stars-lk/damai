package com.damai;

import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;

import java.util.Optional;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream监听
 * @author: 阿宽不是程序员
 **/
@Slf4j
@AllArgsConstructor
public class RedisStreamListener implements StreamListener<String, ObjectRecord<String, String>> {
    
    private final StringRedisTemplate stringRedisTemplate;
    
    private final RedisStreamConfigProperties redisStreamConfigProperties;
    
    private final ApplicationContext applicationContext;

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void onMessage(ObjectRecord<String, String> message) {
        try{
            //消息id
            RecordId messageId = message.getId();
            //消息
            String value = message.getValue();
            log.info("redis stream 消费到了数据 messageId : {}, streamName : {}, message : {}", messageId, message.getStream(), value);
            //处理消息
            Optional.of(applicationContext.getBean(MessageConsumer.class))
                    .orElseThrow(() -> new DaMaiFrameException(BaseCode.MESSAGE_CONSUMER_NOT_EXIST)).accept(message);
            //手动提交ack
            this.stringRedisTemplate.opsForStream().acknowledge(redisStreamConfigProperties.getConsumerGroup(), message);
        }catch (Exception e){
            log.error("onMessage error",e);
        }
    }
}
