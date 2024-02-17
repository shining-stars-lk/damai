package com.damai.service.pagestrategy;

import com.damai.service.pagestrategy.impl.SelectPageDbHandle;
import lombok.AllArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class SelectPageStrategyContext {
    
    private final ConfigurableApplicationContext applicationContext;
    private final Map<String,SelectPageHandle> selectPageHandleMap = new HashMap<>();
    
    public void put(String type,SelectPageHandle selectPageHandle){
        selectPageHandleMap.put(type,selectPageHandle);
    }
    
    public SelectPageHandle get(String type){
        return Optional.ofNullable(selectPageHandleMap.get(type)).orElse(applicationContext.getBean(SelectPageDbHandle.class));
    }
}
