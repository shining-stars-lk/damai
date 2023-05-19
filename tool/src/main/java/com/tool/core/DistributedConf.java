package com.tool.core;

import com.tool.servicelock.ServiceLockInfoProvider;
import com.tool.servicelock.aspect.ServiceLockAspect;
import com.tool.servicelock.redisson.config.RedissonAutoConfiguration;
import com.tool.servicelock.redisson.factory.RedissonLockFactory;
import com.tool.servicelock.util.DistributedLockUtil;
import com.tool.operate.Operate;
import com.tool.operate.impl.RedissonOperate;
import com.tool.repeatLimit.aspect.RepeatLimitAspect;
import com.tool.repeatLimit.info.RepeatLimitInfoProvider;
import com.tool.repeatLimit.info.strategy.generateKey.GenerateKeyHandler;
import com.tool.repeatLimit.info.strategy.generateKey.impl.ParameterGenerateKeyStrategy;
import com.tool.repeatLimit.info.strategy.generateKey.impl.SimpleGenerateKeyStrategy;
import com.tool.repeatLimit.info.strategy.repeatrejected.RepeatLimitHandler;
import com.tool.repeatLimit.info.strategy.repeatrejected.RepeatLimitStrategyFactory;
import com.tool.repeatLimit.info.strategy.repeatrejected.impl.AbortStrategy;
import com.tool.repeatLimit.info.strategy.repeatrejected.impl.SameResultStrategy;
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
    public ServiceLockInfoProvider distributedLockInfoProvider(){
        return new ServiceLockInfoProvider();
    }
    
    @Bean
    public ServiceLockAspect distributedLockAspect(RedissonLockFactory redissonLockFactory, ServiceLockInfoProvider distributedLockInfoProvider){
        return new ServiceLockAspect(redissonLockFactory,distributedLockInfoProvider);
    }
    
    @Bean
    public RepeatLimitInfoProvider repeatLimitInfoProvider(){
        return new RepeatLimitInfoProvider();
    }
    
    @Bean
    public RepeatLimitStrategyFactory repeatLimitStrategyFactory() {
        return new RepeatLimitStrategyFactory();
    }
    
    @Bean
    public RepeatLimitAspect repeatLimitAspect(RepeatLimitInfoProvider repeatLimitInfoProvider, RedissonLockFactory redissonLockFactory,
                                               RepeatLimitStrategyFactory repeatLimitStrategyFactory){
        return new RepeatLimitAspect(repeatLimitInfoProvider,redissonLockFactory,repeatLimitStrategyFactory);
    }
    
    @Bean
    public GenerateKeyHandler parameterGenerateKeyStrategy(RepeatLimitInfoProvider repeatLimitInfoProvider){
        return new ParameterGenerateKeyStrategy(repeatLimitInfoProvider);
    }
    
    @Bean
    public GenerateKeyHandler simpleGenerateKeyStrategy(RepeatLimitInfoProvider repeatLimitInfoProvider){
        return new SimpleGenerateKeyStrategy(repeatLimitInfoProvider);
    }
    
    @Bean
    public RepeatLimitHandler abortStrategy(){
        return new AbortStrategy();
    }
    
    @Bean
    public RepeatLimitHandler sameResultStrategy(Operate redissonOperate){
        return new SameResultStrategy(redissonOperate);
    }
    
    @Bean
    public DistributedLockUtil distributedLockUtil(RedissonLockFactory redissonLockFactory, ServiceLockInfoProvider distributedLockInfoProvider){
        return new DistributedLockUtil(redissonLockFactory,distributedLockInfoProvider);
    }
    
    @Bean
    public Operate redissonOperate(RedissonClient redissonClient){
        return new RedissonOperate(redissonClient);
    }
}
