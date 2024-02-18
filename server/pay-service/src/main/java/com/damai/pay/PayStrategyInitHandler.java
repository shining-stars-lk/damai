package com.damai.pay;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 支付策略初始化
 * @author: 阿宽不是程序员
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
