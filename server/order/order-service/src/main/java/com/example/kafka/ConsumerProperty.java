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
@ConfigurationProperties(prefix = ConsumerProperty.PREFIX)
public class ConsumerProperty {
    
    public static final String PREFIX = "kafka.consumer";
    
    private String servers = "kafka-server1:9093,kafka-server2:9093,kafka-server3:9093";

    private boolean autoCommit;
    
    private String autoCommitIntervalMs;
    
    private String autoOffsetReset;
    
    private String groupId;
    
    private String topic;
}
