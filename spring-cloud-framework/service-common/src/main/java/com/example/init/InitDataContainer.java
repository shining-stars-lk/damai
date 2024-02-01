package com.example.init;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InitDataContainer {
    
    /**
     * 初始化数据
     * */
    public void initData(ConfigurableApplicationContext applicationContext){
        // 获取所有 InitData 类型的 Bean
        Map<String, InitData> initDataMap = applicationContext.getBeansOfType(InitData.class);
        initDataMap.forEach((k,v) -> v.init());
    }
}
