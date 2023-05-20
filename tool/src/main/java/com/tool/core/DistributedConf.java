package com.tool.core;

import com.tool.servicelock.ServiceLockInfoProvider;
import com.tool.servicelock.aspect.ServiceLockAspect;
import com.tool.servicelock.redisson.config.RedissonAutoConfiguration;
import com.tool.servicelock.redisson.factory.RedissonLockFactory;
import com.tool.servicelock.util.ServiceLockUtil;
import com.tool.operate.Operate;
import com.tool.operate.impl.RedissonOperate;
import com.tool.multiplesubmitlimit.aspect.MultipleSubmitLimitAspect;
import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitInfoProvider;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyHandler;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.impl.ParameterGenerateKeyStrategy;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.impl.SimpleGenerateKeyStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitHandler;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitStrategyFactory;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.impl.RejectStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.impl.SameResultStrategy;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-02-23
 **/
@AutoConfigureAfter(RedissonAutoConfiguration.class)
public class DistributedConf {
    
    @Bean
    public RedissonLockFactory redissonLockFactory(RedissonClient redissonClient){
        return new RedissonLockFactory(redissonClient);
    }
    
    @Bean
    public ServiceLockInfoProvider serviceLockInfoProvider(){
        return new ServiceLockInfoProvider();
    }
    
    @Bean
    public ServiceLockAspect serviceLockAspect(RedissonLockFactory redissonLockFactory, ServiceLockInfoProvider serviceLockInfoProvider){
        return new ServiceLockAspect(redissonLockFactory,serviceLockInfoProvider);
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
    public MultipleSubmitLimitAspect multipleSubmitLimitAspect(MultipleSubmitLimitInfoProvider repeatLimitInfoProvider, RedissonLockFactory redissonLockFactory,
                                                       MultipleSubmitLimitStrategyFactory repeatLimitStrategyFactory){
        return new MultipleSubmitLimitAspect(repeatLimitInfoProvider,redissonLockFactory,repeatLimitStrategyFactory);
    }
    
    @Bean
    public GenerateKeyHandler parameterGenerateKeyStrategy(MultipleSubmitLimitInfoProvider repeatLimitInfoProvider){
        return new ParameterGenerateKeyStrategy(repeatLimitInfoProvider);
    }
    
    @Bean
    public GenerateKeyHandler simpleGenerateKeyStrategy(MultipleSubmitLimitInfoProvider repeatLimitInfoProvider){
        return new SimpleGenerateKeyStrategy(repeatLimitInfoProvider);
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
    public ServiceLockUtil serviceLockUtil(RedissonLockFactory redissonLockFactory, ServiceLockInfoProvider distributedLockInfoProvider){
        return new ServiceLockUtil(redissonLockFactory,distributedLockInfoProvider);
    }
    
    @Bean
    public Operate redissonOperate(RedissonClient redissonClient){
        return new RedissonOperate(redissonClient);
    }
}
