package com.damai.init;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 初始化操作执行
 * @author: 阿宽不是程序员
 **/
public class InitDataContainer {
    
    /**
     * 初始化数据
     * */
    public void initData(ConfigurableApplicationContext applicationContext){
        // 获取所有 InitData 类型的 Bean
        Map<String, InitData> initDataMap = applicationContext.getBeansOfType(InitData.class);
        List<InitData> initDataList = 
                initDataMap.values().stream().sorted(Comparator.comparingInt(InitData::executeOrder))
                        .collect(Collectors.toList());
        initDataList.forEach(InitData::init);
    }
}
