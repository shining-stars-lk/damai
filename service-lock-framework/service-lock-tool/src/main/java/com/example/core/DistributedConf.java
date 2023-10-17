package com.example.core;

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
import com.example.servicelock.ServiceLockInfo;
import com.example.servicelock.aspect.ServiceLockAspect;
import com.example.redisson.config.RedissonAutoConfiguration;
import com.example.redisson.factory.ServiceLockFactory;
import com.example.util.ServiceLockUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-02-23
 **/
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class DistributedConf {
    
    @Bean
    public ServiceLockFactory redissonLockFactory(RedissonClient redissonClient){
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
    public ServiceLockUtil serviceLockUtil(ServiceLockFactory serviceLockFactory, ServiceLockInfo serviceLockInfo){
        return new ServiceLockUtil(serviceLockFactory,serviceLockInfo);
    }
    
    @Bean
    public Operate redissonOperate(RedissonClient redissonClient){
        return new RedissonOperate(redissonClient);
    }
}
