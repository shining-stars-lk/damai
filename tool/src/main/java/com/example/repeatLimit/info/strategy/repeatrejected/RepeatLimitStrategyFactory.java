package com.example.repeatLimit.info.strategy.repeatrejected;

import java.util.Optional;

/**
 * @program: distribute-cache
 * @description: 防重复提交触发时策略工厂
 * @author: lk
 * @create: 2022-05-28
 **/
public class RepeatLimitStrategyFactory {

    public RepeatLimitHandler getRepeatLimitStrategy(String key){
        return Optional.ofNullable(RepeatLimitStrategyContext.get(key))
                .orElseThrow(() -> new RuntimeException("执行的拒绝策略不存在"));
    }
}