package com.damai.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: kafka 生产者配置属性
 * @author: 阿宽不是程序员
 **/
@Data
@ConfigurationProperties(prefix = ProducerProperty.PREFIX)
public class ProducerProperty {
    
    public static final String PREFIX = "kafka.producer";
    
    private String servers;
    private int retries = 1;
    private String topic;

}
