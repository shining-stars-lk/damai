package com.damai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

import static com.damai.config.DelayQueueProperties.PREFIX;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 延迟队列 配置属性
 * @author: 阿星不是程序员
 **/
@Data
@ConfigurationProperties(prefix = PREFIX)
public class DelayQueueProperties {

    public static final String PREFIX = "delay.queue";
    
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
}
