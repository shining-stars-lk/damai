package com.example.distributecache.repeatLimit.info.strategy.repeatrejected.impl;

import com.example.distributecache.repeatLimit.info.RepeatRejectedStrategy;
import com.example.distributecache.repeatLimit.info.strategy.repeatrejected.RepeatLimitHandler;
import com.example.distributecache.repeatLimit.info.strategy.repeatrejected.RepeatLimitStrategyContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: 防重复提交触发时策略(快速失败)
 * @author: lk
 * @create: 2022-05-28
 **/
@Component("abortStrategy")
public class AbortStrategy implements RepeatLimitHandler {

    @PostConstruct
    private void init(){
        RepeatLimitStrategyContext.put(RepeatRejectedStrategy.ABORT_STRATEGY.getMsg(),this);
    }


    @Override
    public Object execute(String resultKeyName, long timeOut, TimeUnit timeUnit, ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed();
        return o;
    }

    @Override
    public Object repeatRejected(String resultKeyName) {
        throw new RuntimeException("请不要重复提交");
    }
}
