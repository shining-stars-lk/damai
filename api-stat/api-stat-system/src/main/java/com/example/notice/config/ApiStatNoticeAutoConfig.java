package com.example.notice.config;

import com.example.notice.ApiStatNotice;
import com.example.notice.Platform;
import com.example.redis.RedisCache;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-07
 **/
@Slf4j
@Data
@EnableConfigurationProperties(ApiStatNoticeProperties.class)
@ConditionalOnProperty(value = "api.enable",havingValue = "true")
public class ApiStatNoticeAutoConfig {
    
    @Bean
    public List<Platform> platformList(ApiStatNoticeProperties apiStatNoticeProperties){
        PlatformRegistry platformRegistry = new PlatformRegistry(apiStatNoticeProperties);
        return platformRegistry.createPlatforms();
    }
    
    @Bean
    public ApiStatNotice apiStatNotice(ApiStatNoticeProperties apiStatNoticeProperties, RedisCache redisCache, List<Platform> platformList){
        return new ApiStatNotice(apiStatNoticeProperties,redisCache,platformList);
    }
    
}
