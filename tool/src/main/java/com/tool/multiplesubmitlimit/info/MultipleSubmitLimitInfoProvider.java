package com.tool.multiplesubmitlimit.info;

import com.tool.core.BaseInfoProvider;
import com.tool.multiplesubmitlimit.annotion.MultipleSubmitLimit;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyHandler;
import com.tool.multiplesubmitlimit.info.strategy.generateKey.GenerateKeyStrategyContext;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @program: distribute-cache
 * @description: 防重复提交标识组装类
 * @author: k
 * @create: 2022-05-28
 **/
public class MultipleSubmitLimitInfoProvider extends BaseInfoProvider {

    private final Logger logger = LoggerFactory.getLogger(MultipleSubmitLimitInfoProvider.class);

    public static final String NAME_PREFIX = "REPEAT_LIMIT";

    public static final String RESULT_PREFIX = "REPEAT_RESULT";

    public String getLockName(JoinPoint joinPoint,String name,String[] keys){
        return NAME_PREFIX + ":" + name + getDefinitionKey(joinPoint, keys);
    }

    public String getResultKeyName(JoinPoint joinPoint,String name,String[] keys){
        return RESULT_PREFIX + ":" + name + getDefinitionKey(joinPoint, keys);
    }

    public GenerateKeyHandler getGenerateKeyStrategy(String generatorKey){
        return Optional.ofNullable(GenerateKeyStrategyContext.get(generatorKey))
                .orElseThrow(() -> new RuntimeException("执行的生成策略不存在"));
    }

    /**
     * 获取指定生成业务策略生成的业务名字
     * */
    public String getLockNameByGenerateKeyStrategy(MultipleSubmitLimit repeatLimit, JoinPoint joinPoint){
        GenerateKeyHandler generateKeyStrategy = getGenerateKeyStrategy(repeatLimit.generatorKey().getMsg());
        String key = generateKeyStrategy.generateKey(joinPoint);
        return MultipleSubmitLimitInfoProvider.NAME_PREFIX.concat(":").concat(key);
    }


    public String getResultKeyNameByGenerateKeyStrategy(MultipleSubmitLimit repeatLimit, JoinPoint joinPoint){
        GenerateKeyHandler generateKeyStrategy = getGenerateKeyStrategy(repeatLimit.generatorKey().getMsg());
        String key = generateKeyStrategy.generateKey(joinPoint);
        return MultipleSubmitLimitInfoProvider.RESULT_PREFIX.concat(":").concat(key);
    }
}
