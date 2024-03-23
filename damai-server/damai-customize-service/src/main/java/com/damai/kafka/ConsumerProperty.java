package com.damai.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: kafka 消费配置属性
 * @author: 阿宽不是程序员
 **/
@Data
@ConfigurationProperties(prefix = ConsumerProperty.PREFIX)
public class ConsumerProperty {
    
    public static final String PREFIX = "kafka.consumer";
    
    private String servers;

    private boolean autoCommit;
    
    private String autoCommitIntervalMs;
    
    private String autoOffsetReset;
    
    private String groupId;
    
    private String topic;
}
