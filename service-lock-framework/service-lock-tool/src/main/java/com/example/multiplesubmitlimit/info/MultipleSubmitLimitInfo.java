package com.example.multiplesubmitlimit.info;

import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;
import com.example.core.BaseInfo;
import com.example.multiplesubmitlimit.annotion.MultipleSubmitLimit;
import com.example.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyHandler;
import com.example.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyStrategyContext;
import org.aspectj.lang.JoinPoint;

import java.util.Optional;

import static com.example.core.Constants.SEPARATOR;

/**
 * @program: redis-example
 * @description: 防重复提交标识组装类
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class MultipleSubmitLimitInfo extends BaseInfo {

    public static final String NAME_PREFIX = "REPEAT_LIMIT";

    public static final String RESULT_PREFIX = "REPEAT_RESULT";

    public String getLockName(JoinPoint joinPoint,String name,String[] keys){
        return NAME_PREFIX + SEPARATOR + name + getRelKey(joinPoint, keys);
    }

    public String getResultKeyName(JoinPoint joinPoint,String name,String[] keys){
        return RESULT_PREFIX + SEPARATOR + name + getRelKey(joinPoint, keys);
    }

    public GenerateKeyHandler getGenerateKeyStrategy(String generatorKey){
        return Optional.ofNullable(GenerateKeyStrategyContext.get(generatorKey))
                .orElseThrow(() -> new ToolkitException(BaseCode.GENERATE_STRATEGY_NOT_EXIST));
    }

    /**
     * 获取指定生成业务策略生成的业务名字
     * */
    public String getLockNameByGenerateKeyStrategy(MultipleSubmitLimit repeatLimit, JoinPoint joinPoint){
        GenerateKeyHandler generateKeyStrategy = getGenerateKeyStrategy(repeatLimit.generatorKey().getMsg());
        String key = generateKeyStrategy.generateKey(joinPoint);
        return MultipleSubmitLimitInfo.NAME_PREFIX.concat(SEPARATOR).concat(key);
    }


    public String getResultKeyNameByGenerateKeyStrategy(MultipleSubmitLimit repeatLimit, JoinPoint joinPoint){
        GenerateKeyHandler generateKeyStrategy = getGenerateKeyStrategy(repeatLimit.generatorKey().getMsg());
        String key = generateKeyStrategy.generateKey(joinPoint);
        return MultipleSubmitLimitInfo.RESULT_PREFIX.concat(SEPARATOR).concat(key);
    }
}
