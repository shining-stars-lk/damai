package com.example.service.init;

import com.example.init.InitData;
import com.example.service.ProgramShowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-31
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
}
