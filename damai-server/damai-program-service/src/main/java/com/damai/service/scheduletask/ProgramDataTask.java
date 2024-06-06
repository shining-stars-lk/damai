package com.damai.service.scheduletask;

import com.damai.service.init.ProgramElasticsearchInitData;
import com.damai.service.init.ProgramShowTimeRenewal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-06-05
 **/
@Component
public class ProgramDataTask {
    
    @Autowired
    private ConfigurableApplicationContext applicationContext;
    
    @Autowired
    private ProgramShowTimeRenewal programShowTimeRenewal;
    
    @Autowired
    private ProgramElasticsearchInitData programElasticsearchInitData;

    
    public void executeTask(){
        programShowTimeRenewal.executeInit(applicationContext);
        programElasticsearchInitData.executeInit(applicationContext);
    }
}
