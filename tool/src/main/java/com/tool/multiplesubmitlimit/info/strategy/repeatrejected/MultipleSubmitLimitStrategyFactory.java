package com.tool.multiplesubmitlimit.info.strategy.repeatrejected;

import com.example.enums.BaseCode;
import com.example.exception.ToolkitException;

import java.util.Optional;

/**
 * @program: redis-tool
 * @description: 防重复提交触发时策略工厂
 * @author: 星哥
 * @create: 2023-05-28
 **/
public class MultipleSubmitLimitStrategyFactory {

    public MultipleSubmitLimitHandler getMultipleSubmitLimitStrategy(String key){
        return Optional.ofNullable(MultipleSubmitLimitStrategyContext.get(key))
                .orElseThrow(() -> new ToolkitException(BaseCode.REJECT_STRATEGY_NOT_EXIST));
    }
}
