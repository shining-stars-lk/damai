package com.damai.service.pagestrategy;

import com.damai.service.pagestrategy.impl.SelectPageDbHandle;
import lombok.AllArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目查询策略上下文
 * @author: 阿宽不是程序员
 **/
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
