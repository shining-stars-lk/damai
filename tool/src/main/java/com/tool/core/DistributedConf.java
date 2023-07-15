package com.tool.core;

import com.tool.multiplesubmitlimit.aspect.MultipleSubmitLimitAspect;
import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitInfoProvider;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyHandler;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.impl.ParameterGenerateKeyStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitHandler;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitStrategyFactory;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.impl.RejectStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.impl.SameResultStrategy;
import com.tool.operate.Operate;
import com.tool.operate.impl.RedissonOperate;
import com.tool.servicelock.ServiceLockInfoProvider;
import com.tool.servicelock.aspect.ServiceLockAspect;
import com.tool.servicelock.redisson.config.RedissonAutoConfiguration;
import com.tool.servicelock.redisson.factory.ServiceLockFactory;
import com.tool.servicelock.util.ServiceLockUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-02-23
 **/
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class DistributedConf {
    
    @Bean
    public ServiceLockFactory redissonLockFactory(RedissonClient redissonClient){
        return new ServiceLockFactory(redissonClient);
    }
    
    @Bean
    public ServiceLockInfoProvider serviceLockInfoProvider(){
        return new ServiceLockInfoProvider();
    }
    
    @Bean
    public ServiceLockAspect serviceLockAspect(ServiceLockFactory serviceLockFactory, ServiceLockInfoProvider serviceLockInfoProvider){
        return new ServiceLockAspect(serviceLockFactory,serviceLockInfoProvider);
    }
    
    @Bean
    public MultipleSubmitLimitInfoProvider multipleSubmitLimitInfoProvider(){
        return new MultipleSubmitLimitInfoProvider();
    }
    
    @Bean
    public MultipleSubmitLimitStrategyFactory multipleSubmitLimitStrategyFactory() {
        return new MultipleSubmitLimitStrategyFactory();
    }
    
    @Bean
    public MultipleSubmitLimitAspect multipleSubmitLimitAspect(MultipleSubmitLimitInfoProvider repeatLimitInfoProvider, ServiceLockFactory serviceLockFactory,
                                                       MultipleSubmitLimitStrategyFactory repeatLimitStrategyFactory){
        return new MultipleSubmitLimitAspect(repeatLimitInfoProvider, serviceLockFactory,repeatLimitStrategyFactory);
    }
    
    @Bean
    public GenerateKeyHandler parameterGenerateKeyStrategy(MultipleSubmitLimitInfoProvider repeatLimitInfoProvider){
        return new ParameterGenerateKeyStrategy(repeatLimitInfoProvider);
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
    public ServiceLockUtil serviceLockUtil(ServiceLockFactory serviceLockFactory, ServiceLockInfoProvider distributedLockInfoProvider){
        return new ServiceLockUtil(serviceLockFactory,distributedLockInfoProvider);
    }
    
    @Bean
    public Operate redissonOperate(RedissonClient redissonClient){
        return new RedissonOperate(redissonClient);
    }
}
