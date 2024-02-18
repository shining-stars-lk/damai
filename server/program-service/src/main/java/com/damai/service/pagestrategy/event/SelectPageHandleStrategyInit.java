package com.damai.service.pagestrategy.event;

import com.damai.service.pagestrategy.SelectPageHandle;
import com.damai.service.pagestrategy.SelectPageStrategyContext;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目查询策略初始化
 * @author: 阿宽不是程序员
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
