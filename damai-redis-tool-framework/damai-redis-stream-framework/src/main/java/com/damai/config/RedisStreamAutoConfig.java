package com.damai.config;

import com.damai.RedisStreamConfigProperties;
import com.damai.RedisStreamListener;
import com.damai.RedisStreamPushHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: redis-stream配置
 * @author: 阿宽不是程序员
 **/
@EnableConfigurationProperties(RedisStreamConfigProperties.class)
public class RedisStreamAutoConfig {
    
    @Bean
    public RedisStreamPushHandler redisStreamPushHandler(StringRedisTemplate stringRedisTemplate,
                                                         RedisStreamConfigProperties redisStreamConfigProperties){
        return new RedisStreamPushHandler(stringRedisTemplate,redisStreamConfigProperties);
    }
    
    @Bean
    public RedisStreamListener redisStreamListener(StringRedisTemplate stringRedisTemplate, 
                                                   RedisStreamConfigProperties redisStreamConfigProperties,
                                                   ApplicationContext applicationContext){
        return new RedisStreamListener(stringRedisTemplate,redisStreamConfigProperties,applicationContext);
    }
}
