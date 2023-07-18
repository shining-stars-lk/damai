package com.example.kafka;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-14
 **/
@AllArgsConstructor
public class OrderMessageSend {
    
    private KafkaTemplate<String, String> kafkaTemplate;
    
    private String topic;
    
    public void sendMessage(String message) {
        kafkaTemplate.send(topic,message);
    }
}
