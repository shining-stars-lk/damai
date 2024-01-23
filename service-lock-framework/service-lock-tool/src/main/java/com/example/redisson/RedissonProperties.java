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
    
    /**
     * 从队列拉取数据的线程池中的核心线程数量，如果业务过慢可调大
     * */
    private Integer corePoolSize = 4;
    /**
     * 从队列拉取数据的线程池中的最大线程数量，如果业务过慢可调大
     * */
    private Integer maximumPoolSize = 4;
    
    /**
     * 从队列拉取数据的线程池中的最大线程回收时间
     * */
    private long keepAliveTime = 30;
    /**
     * 从队列拉取数据的线程池中的最大线程回收时间的时间单位
     * */
    private TimeUnit unit = TimeUnit.SECONDS;
    /**
     * 从队列拉取数据的线程池中的队列数量，如果业务过慢可调大
     * */
    private Integer workQueueSize = 256;
    
    /**
     * 延时队列的隔离分区数，延时有瓶颈时 可调大次数，但会增大redis的cpu消耗(同一个topic发送者和消费者的隔离分区数必须相同)
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
