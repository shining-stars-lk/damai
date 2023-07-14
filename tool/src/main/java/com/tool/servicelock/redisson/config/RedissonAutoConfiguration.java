package com.tool.servicelock.redisson.config;

import com.example.core.StringUtil;
import com.tool.core.DistributedConf;
import com.tool.servicelock.redisson.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: tool
 * @description: redisson信息配置加载类
 * @author: kuan
 * @create: 2023-05-28
 **/
@ConditionalOnProperty("redisson.address")
@EnableConfigurationProperties(RedissonProperties.class)
@AutoConfigureAfter(RedisAutoConfiguration.class)
@AutoConfigureBefore(DistributedConf.class)
@Slf4j
public class RedissonAutoConfiguration {

    private static final String ADDRESS_PREFIX = "redis";

    /**
     * 单机模式自动装配
     * @return RedissonClient
     */
    @Bean
    public RedissonClient redissonSingle(RedissonProperties redissonProperties) {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(ADDRESS_PREFIX+"://"+redissonProperties.getAddress()+":"+redissonProperties.getPort())
                .setDatabase(redissonProperties.getDatabase())
                .setTimeout(redissonProperties.getTimeout())
                .setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize())
                .setIdleConnectionTimeout(30000)
                .setPingConnectionInterval(30000);

        if(StringUtil.isNotEmpty(redissonProperties.getPassword())) {
            serverConfig.setPassword(redissonProperties.getPassword());
        }
        return Redisson.create(config);
    }
}
