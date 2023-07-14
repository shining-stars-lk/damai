package com.tool.multiplesubmitlimit.info.strategy.repeatrejected;

import java.util.Optional;

/**
 * @program: redis-tool
 * @description: 防重复提交触发时策略工厂
 * @author: kuan
 * @create: 2023-05-28
 **/
public class MultipleSubmitLimitStrategyFactory {

    public MultipleSubmitLimitHandler getMultipleSubmitLimitStrategy(String key){
        return Optional.ofNullable(MultipleSubmitLimitStrategyContext.get(key))
                .orElseThrow(() -> new RuntimeException("执行的拒绝策略不存在"));
    }
}
