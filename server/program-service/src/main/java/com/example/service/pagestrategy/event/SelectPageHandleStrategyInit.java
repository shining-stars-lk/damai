package com.example.service.pagestrategy.event;

import com.example.service.pagestrategy.SelectPageHandle;
import com.example.service.pagestrategy.SelectPageStrategyContext;
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
public class SelectPageHandleStrategyInit implements ApplicationListener<ApplicationStartedEvent> {
    
    private final SelectPageStrategyContext selectPageStrategyContext;
    
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, SelectPageHandle> selectPageHandleMap = event.getApplicationContext().getBeansOfType(SelectPageHandle.class);
        for (Entry<String, SelectPageHandle> entry : selectPageHandleMap.entrySet()) {
            SelectPageHandle selectPageHandle = entry.getValue();
            selectPageStrategyContext.put(selectPageHandle.getType(),selectPageHandle);
        }
    }
}
