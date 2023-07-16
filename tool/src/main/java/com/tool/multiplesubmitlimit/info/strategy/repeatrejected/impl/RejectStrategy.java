package com.tool.multiplesubmitlimit.info.strategy.repeatrejected.impl;

import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitRejectedStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitHandler;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitStrategyContext;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: 防重复提交触发时策略(快速失败)
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class RejectStrategy implements MultipleSubmitLimitHandler {

    @PostConstruct
    private void init(){
        MultipleSubmitLimitStrategyContext.put(MultipleSubmitLimitRejectedStrategy.ABORT_STRATEGY.getMsg(),this);
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
