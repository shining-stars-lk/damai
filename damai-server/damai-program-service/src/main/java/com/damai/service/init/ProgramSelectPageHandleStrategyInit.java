package com.damai.service.init;

import com.damai.initialize.base.AbstractApplicationPostConstructHandler;
import com.damai.service.pagestrategy.SelectPageHandle;
import com.damai.service.pagestrategy.SelectPageStrategyContext;
import lombok.AllArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;
import java.util.Map.Entry;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目查询策略初始化
 * @author: 阿宽不是程序员
 **/
@AllArgsConstructor
public class ProgramSelectPageHandleStrategyInit extends AbstractApplicationPostConstructHandler {
    
    private final SelectPageStrategyContext selectPageStrategyContext;
    
    
    @Override
    public Integer executeOrder() {
        return 4;
    }
    
    @Override
    public void executeInit(ConfigurableApplicationContext context) {
        Map<String, SelectPageHandle> selectPageHandleMap = context.getBeansOfType(SelectPageHandle.class);
        for (Entry<String, SelectPageHandle> entry : selectPageHandleMap.entrySet()) {
            SelectPageHandle selectPageHandle = entry.getValue();
            selectPageStrategyContext.put(selectPageHandle.getType(),selectPageHandle);
        }
    }
}
