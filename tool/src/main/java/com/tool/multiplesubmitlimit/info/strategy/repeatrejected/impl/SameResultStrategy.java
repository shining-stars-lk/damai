package com.tool.multiplesubmitlimit.info.strategy.repeatrejected.impl;

import com.tool.operate.Operate;
import com.tool.multiplesubmitlimit.info.MultipleSubmitLimitRejectedStrategy;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitHandler;
import com.tool.multiplesubmitlimit.info.strategy.repeatrejected.MultipleSubmitLimitStrategyContext;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @program: redis-tool
 * @description: 防重复提交触发时策略(方法执行期间返回相同结果)
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class SameResultStrategy implements MultipleSubmitLimitHandler {

    private final String NO_RETURN_VALUE = "void";
    
    private final Operate operate;
    
    public SameResultStrategy(Operate operate){
        this.operate = operate;
    }

    @PostConstruct
    private void init(){
        MultipleSubmitLimitStrategyContext.put(MultipleSubmitLimitRejectedStrategy.SAME_RESULT.getMsg(),this);
    }


    @Override
    public Object execute(String resultKeyName, long timeOut, TimeUnit timeUnit, ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = operate.get(resultKeyName);
        if (o != null) {
            if (NO_RETURN_VALUE.equals(o)) {
                o = null;
            }
            return o;
        }else{
            Object proceed,resultObject = null;
            try{
                proceed = joinPoint.proceed();
                resultObject = proceed;
                if (resultObject == null) {
                    resultObject = NO_RETURN_VALUE;
                }
                return proceed;
            }finally {
                operate.set(resultKeyName,resultObject,timeOut,timeUnit);
            }
        }
    }

    @Override
    public Object repeatRejected(String resultKeyName) {
        Object o = operate.get(resultKeyName);
        if (NO_RETURN_VALUE.equals(o)) {
            o = null;
        }
        return o;
    }
}
