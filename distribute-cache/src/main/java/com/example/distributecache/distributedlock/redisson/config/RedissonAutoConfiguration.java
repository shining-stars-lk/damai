package com.example.distributecache.distributedlock.redisson.config;

import com.example.distributecache.core.StringUtil;
import com.example.distributecache.distributedlock.redisson.RedissonProperties;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: distribute-cache
 * @description: redisson信息配置加载类
 * @author: lk
 * @create: 2022-05-28
 **/
@Configuration
/** 当yml中的配置存在 redisson.address和redisson.password时 此配置对象才生效，否则会报错
 * */
//@ConditionalOnProperty({"redisson.address","redisson.password"})
/** 让使用@ConfigurationProperties 注解的类生效。
 *  说明：
 *      如果一个配置类只配置@ConfigurationProperties注解，而没有使用@Component，
 *      那么在IOC容器中是获取不到properties 配置文件转化的bean。
 *      说白了 @EnableConfigurationProperties 相当于把使用 @ConfigurationProperties 的类进行了一次注入。
 **/
@EnableConfigurationProperties(RedissonProperties.class)
/**让RedisAutoConfiguration加载完后再加载RedissonAutoConfiguration
 * */
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedissonAutoConfiguration {

    private Logger logger = LoggerFactory.getLogger(RedissonAutoConfiguration.class);

    @Autowired
    private RedissonProperties redssionProperties;

    private static final String ADDRESS_PREFIX = "redis";

    /**
     * 单机模式自动装配
     * @return RedissonClient
     */
    @Bean
    RedissonClient redissonSingle() {

        verify(redssionProperties);

        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(ADDRESS_PREFIX+"://"+redssionProperties.getAddress()+":"+redssionProperties.getPort())
                .setDatabase(redssionProperties.getDatabase())
                .setTimeout(redssionProperties.getTimeout())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize())
                .setIdleConnectionTimeout(30000)
                .setPingConnectionInterval(30000);

        if(StringUtil.isNotEmpty(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }

        return Redisson.create(config);
    }

    private void verify(RedissonProperties redssionProperties){
        if (redssionProperties == null) {
            throw new RuntimeException("属性为空");
        }
        if (StringUtil.isEmpty(redssionProperties.getAddress())){
            throw new RuntimeException("地址为空");
        }
        if (StringUtil.isEmpty(redssionProperties.getPassword())){
            throw new RuntimeException("密码为空");
        }
    }
}
