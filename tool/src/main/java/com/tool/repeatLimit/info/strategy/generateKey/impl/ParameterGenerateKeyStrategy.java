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
import java.util.StringJoiner;

/**
 * @program: distribute-cache
 * @description: 生成键通用策略 REPEAT_LIMIT(标识)+类名+方法名+参数名+userId
 * @author: lk
 * @create: 2022-05-28
 **/
public class ParameterGenerateKeyStrategy implements GenerateKeyHandler {
    
    private RepeatLimitInfoProvider repeatLimitInfoProvider;
    
    public ParameterGenerateKeyStrategy(RepeatLimitInfoProvider repeatLimitInfoProvider){
        this.repeatLimitInfoProvider = repeatLimitInfoProvider;
    }

    @PostConstruct
    public void init(){
        GenerateKeyStrategyContext.put(GenerateKeyStrategy.PARAMETER_GENERATE_KEY_STRATEGY.getMsg(),this);
    }

    /**
     * 通用策略 REPEAT_LIMIT(标识)+类名+方法名+参数名+userId
     * */
    @Override
    public String generateKey(JoinPoint joinPoint) {
        HttpServletRequest request = repeatLimitInfoProvider.getRequest();
        String userId = request.getHeader(Constants.REPEAT_LIMIT_USERID);

        Object target = joinPoint.getTarget();
        Method method = repeatLimitInfoProvider.getMethod(joinPoint);

        String key = target.getClass().getName().concat(":").concat(method.getName());
        Object[] params = joinPoint.getArgs();
        if (params != null && params.length > 0) {
            StringJoiner joiner = new StringJoiner(",");
            for (Object param : params) {
                joiner.add(param.getClass().getName());
            }
            key = key.concat(":").concat(joiner.toString());
        }
        if (StringUtil.isNotEmpty(userId)) {
            key = key.concat(":").concat(userId);
        }
        return key;
    }
}
