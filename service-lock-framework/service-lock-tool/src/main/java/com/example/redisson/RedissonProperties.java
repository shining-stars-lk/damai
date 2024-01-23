package com.example.redisson;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-example
 * @description: 将yml的配置和此对象绑定
 * @author: 星哥
 * @create: 2023-05-28
 **/
@Data
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
public class RedissonProperties {

    public static final String PREFIX = "redisson";

//    private int timeout = 3000;
//
//    private String address = "";
//
//    private String port = "6379";
//
//    private String password = "";
//
//    private int database = 2;
//
//    private int connectionPoolSize = 64;
//
//    private int connectionMinimumIdleSize = 10;
    
    /**
     * 从队列拉取数据的线程数量，如果业务过慢可调大
     * */
    private Integer threadCount = 4;
    
    /**
     * 延时队列的隔离分区数，延时有瓶颈时 可调大次数，但会增大redis的cpu消耗
     * */
    private Integer isolationRegionCount = 5;

    private String produceTopic;
    
    private Long delayTime;
    
    private TimeUnit delayTimeUnit;
    
    private String consumeTopic;
    
    private String rbLoomFilterName = "user_register_rb_loom_filter";
    
    private Long expectedInsertions = 20000L;
    
    private Double falseProbability = 0.01D;
}
