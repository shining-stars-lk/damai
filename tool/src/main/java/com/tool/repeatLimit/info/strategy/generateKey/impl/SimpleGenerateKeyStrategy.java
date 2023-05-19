package com.tool.repeatLimit.info.strategy.generateKey.impl;


import com.tool.core.Constants;
import com.example.core.StringUtil;
import com.tool.repeatLimit.info.GenerateKeyStrategy;
import com.tool.repeatLimit.info.RepeatLimitInfoProvider;
import com.tool.repeatLimit.info.strategy.generateKey.GenerateKeyHandler;
import com.tool.repeatLimit.info.strategy.generateKey.GenerateKeyStrategyContext;
import org.aspectj.lang.JoinPoint;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @program: distribute-cache
 * @description: 生成键简单策略 REPEAT_LIMIT(标识)+类名+方法名+userId
 * @author: lk
 * @create: 2022-05-28
 **/
public class SimpleGenerateKeyStrategy implements GenerateKeyHandler {

    
    private RepeatLimitInfoProvider repeatLimitInfoProvider;
    
    public SimpleGenerateKeyStrategy(RepeatLimitInfoProvider repeatLimitInfoProvider){
        this.repeatLimitInfoProvider = repeatLimitInfoProvider;
    }

    @PostConstruct
    public void init(){
        GenerateKeyStrategyContext.put(GenerateKeyStrategy.SIMPLE_GENERATE_KEY_STRATEGY.getMsg(),this);
    }

    /**
     * 简单策略 REPEAT_LIMIT(标识)+类名+方法名+userId
     * */
    @Override
    public String generateKey(JoinPoint joinPoint) {
        HttpServletRequest request = repeatLimitInfoProvider.getRequest();
        String userId = request.getHeader(Constants.REPEAT_LIMIT_USERID);
        Method method = repeatLimitInfoProvider.getMethod(joinPoint);
        Object target = joinPoint.getTarget();
        String fullName = target.getClass().getName() + ":" + method.getName();
        if (StringUtil.isNotEmpty(userId)) {
            fullName = fullName.concat(":").concat(userId);
        }
        return fullName;
    }
}
