package com.tool.servicelock.redisson;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: 将yml的配置和此对象绑定
 * @author: 星哥
 * @create: 2023-05-28
 **/
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
@Data
public class RedissonProperties {

    public static final String PREFIX = "redisson";

    private int timeout = 3000;

    private String address = "";

    private String port = "6379";

    private String password = "";

    private int database = 2;

    private int connectionPoolSize = 64;

    private int connectionMinimumIdleSize = 10;

    private String produceTopic;
    
    private Long delayTime;
    
    private TimeUnit delayTimeUnit;
    
    private String consumeTopic;
}
