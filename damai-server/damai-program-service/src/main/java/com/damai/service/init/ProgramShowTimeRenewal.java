package com.damai.service.init;

import com.damai.init.InitData;
import com.damai.service.ProgramShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目演出时间更新
 * @author: 阿宽不是程序员
 **/
@Component
public class ProgramShowTimeRenewal implements InitData {
    
    @Autowired
    private ProgramShowTimeService programShowTimeService;
    
    /**
     * 项目启动将库中的节目演出时间进行更新，真实生产环境不会这么做的
     * */
    @Override
    public void init() {
        programShowTimeService.renewal();
    }
    
    @Override
    public int executeOrder() {
        return 2;
    }
}
