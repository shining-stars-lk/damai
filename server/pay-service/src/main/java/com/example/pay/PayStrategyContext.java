package com.example.pay;

import com.example.enums.BaseCode;
import com.example.exception.CookFrameException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
public class PayStrategyContext {
    
    private final Map<String,PayStrategyHandler> payStrategyHandlerMap = new HashMap<>();
    
    public void put(String channel,PayStrategyHandler payStrategyHandler){
        payStrategyHandlerMap.put(channel,payStrategyHandler);
    }
    
    public PayStrategyHandler get(String channel){
        return Optional.ofNullable(payStrategyHandlerMap.get(channel)).orElseThrow(
                () -> new CookFrameException(BaseCode.PAY_STRATEGY_NOT_EXIST));
    }
}
