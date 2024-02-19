package com.damai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 分布式锁 配置属性
 * @author: 阿宽不是程序员
 **/
@Data
@ConfigurationProperties(prefix = RedissonProperties.PREFIX)
public class RedissonProperties {

    public static final String PREFIX = "redisson";
    
    private String rbLoomFilterName = "user_register_rb_loom_filter";
    
    private Long expectedInsertions = 20000L;
    
    private Double falseProbability = 0.01D;
}
