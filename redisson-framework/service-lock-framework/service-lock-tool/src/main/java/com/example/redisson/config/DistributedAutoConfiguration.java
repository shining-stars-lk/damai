package com.example.redisson.config;

import com.example.multiplesubmitlimit.aspect.MultipleSubmitLimitAspect;
import com.example.multiplesubmitlimit.info.MultipleSubmitLimitInfo;
import com.example.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyHandler;
import com.example.multiplesubmitlimit.info.strategy.generateKey.impl.ParameterGenerateKeyStrategy;
import com.example.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitHandler;
import com.example.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitStrategyFactory;
import com.example.multiplesubmitlimit.info.strategy.repeatrejected.impl.RejectStrategy;
import com.example.multiplesubmitlimit.info.strategy.repeatrejected.impl.SameResultStrategy;
import com.example.operate.Operate;
import com.example.operate.impl.RedissonOperate;
import com.example.redisson.RedissonProperties;
import com.example.redisson.factory.ServiceLockFactory;
import com.example.servicelock.ServiceLockInfo;
import com.example.servicelock.aspect.ServiceLockAspect;
import com.example.util.RBloomFilterUtil;
import com.example.util.ServiceLockTool;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-02-23
 **/
@EnableConfigurationProperties(RedissonProperties.class)
public class DistributedAutoConfiguration {
    
    @Bean
    public ServiceLockFactory serviceLockFactory(RedissonClient redissonClient){
        return new ServiceLockFactory(redissonClient);
    }
    
    @Bean
    public ServiceLockInfo serviceLockInfo(){
        return new ServiceLockInfo();
    }
    
    @Bean
    public ServiceLockAspect serviceLockAspect(ServiceLockFactory serviceLockFactory, ServiceLockInfo serviceLockInfo){
        return new ServiceLockAspect(serviceLockFactory,serviceLockInfo);
    }
    
    @Bean
    public MultipleSubmitLimitInfo multipleSubmitLimitInfo(){
        return new MultipleSubmitLimitInfo();
    }
    
    @Bean
    public MultipleSubmitLimitStrategyFactory multipleSubmitLimitStrategyFactory() {
        return new MultipleSubmitLimitStrategyFactory();
    }
    
    @Bean
    public MultipleSubmitLimitAspect multipleSubmitLimitAspect(MultipleSubmitLimitInfo multipleSubmitLimitInfo, ServiceLockFactory serviceLockFactory,
                                                               MultipleSubmitLimitStrategyFactory multipleSubmitLimitStrategyFactory){
        return new MultipleSubmitLimitAspect(multipleSubmitLimitInfo, serviceLockFactory,multipleSubmitLimitStrategyFactory);
    }
    
    @Bean
    public GenerateKeyHandler parameterGenerateKeyStrategy(MultipleSubmitLimitInfo multipleSubmitLimitInfo){
        return new ParameterGenerateKeyStrategy(multipleSubmitLimitInfo);
    }
    
    @Bean
    public MultipleSubmitLimitHandler rejectStrategy(){
        return new RejectStrategy();
    }
    
    @Bean
    public MultipleSubmitLimitHandler sameResultStrategy(Operate redissonOperate){
        return new SameResultStrategy(redissonOperate);
    }
    
    @Bean
    public ServiceLockTool serviceLockUtil(ServiceLockFactory serviceLockFactory, ServiceLockInfo serviceLockInfo){
        return new ServiceLockTool(serviceLockFactory,serviceLockInfo);
    }
    
    @Bean
    public Operate redissonOperate(RedissonClient redissonClient){
        return new RedissonOperate(redissonClient);
    }
    
    /**
     * 布隆过滤器
     */
    @Bean
    public RBloomFilterUtil rBloomFilterUtil(RedissonClient redissonClient, RedissonProperties redissonProperties) {
        return new RBloomFilterUtil(redissonClient,redissonProperties);
    }
}
