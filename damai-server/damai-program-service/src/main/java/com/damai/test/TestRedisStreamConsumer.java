package com.damai.test;

import com.damai.MessageConsumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.stereotype.Component;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-05-08
 **/
@Component
public class TestRedisStreamConsumer implements MessageConsumer {
    @Override
    public void accept(final ObjectRecord<String, String> message) {
        String value = message.getValue();
        System.out.println("====处理消息:"+value+"=====");
    }
}
