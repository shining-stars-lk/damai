package com.damai.pay;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@AllArgsConstructor
public class PayStrategyInitHandler implements ApplicationListener<ApplicationStartedEvent> {
    
    private final PayStrategyContext payStrategyContext;
    
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, PayStrategyHandler> payStrategyHandlerMap = event.getApplicationContext().getBeansOfType(PayStrategyHandler.class);
        for (Entry<String, PayStrategyHandler> entry : payStrategyHandlerMap.entrySet()) {
            PayStrategyHandler payStrategyHandler = entry.getValue();
            payStrategyContext.put(payStrategyHandler.getChannel(),payStrategyHandler);
        }
    }
}
