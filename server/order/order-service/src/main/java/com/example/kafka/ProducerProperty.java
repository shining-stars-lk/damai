package com.example.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Data
@ConfigurationProperties(prefix = ProducerProperty.PREFIX)
public class ProducerProperty {
    
    public static final String PREFIX = "kafka.producer";
    
    private String servers = "kafka-server1:9093,kafka-server2:9093,kafka-server3:9093";
    private int retries = 1;
    private String topic;

}
