package com.example.core;

import com.example.distributedlock.DistributedLockInfoProvider;
import com.example.distributedlock.aspect.DistributedLockAspect;
import com.example.distributedlock.redisson.config.RedissonAutoConfiguration;
import com.example.distributedlock.redisson.factory.RedissonLockFactory;
import com.example.distributedlock.util.DistributedLockUtil;
import com.example.operate.Operate;
import com.example.operate.impl.RedissonOperate;
import com.example.repeatLimit.aspect.RepeatLimitAspect;
import com.example.repeatLimit.info.RepeatLimitInfoProvider;
import com.example.repeatLimit.info.strategy.generateKey.GenerateKeyHandler;
import com.example.repeatLimit.info.strategy.generateKey.impl.ParameterGenerateKeyStrategy;
import com.example.repeatLimit.info.strategy.generateKey.impl.SimpleGenerateKeyStrategy;
import com.example.repeatLimit.info.strategy.repeatrejected.RepeatLimitHandler;
import com.example.repeatLimit.info.strategy.repeatrejected.RepeatLimitStrategyFactory;
import com.example.repeatLimit.info.strategy.repeatrejected.impl.AbortStrategy;
import com.example.repeatLimit.info.strategy.repeatrejected.impl.SameResultStrategy;
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
    public DistributedLockInfoProvider distributedLockInfoProvider(){
        return new DistributedLockInfoProvider();
    }
    
    @Bean
    public DistributedLockAspect distributedLockAspect(RedissonLockFactory redissonLockFactory,DistributedLockInfoProvider distributedLockInfoProvider){
        return new DistributedLockAspect(redissonLockFactory,distributedLockInfoProvider);
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
    public DistributedLockUtil distributedLockUtil(RedissonLockFactory redissonLockFactory, DistributedLockInfoProvider distributedLockInfoProvider){
        return new DistributedLockUtil(redissonLockFactory,distributedLockInfoProvider);
    }
    
    @Bean
    public Operate redissonOperate(RedissonClient redissonClient){
        return new RedissonOperate(redissonClient);
    }
}
