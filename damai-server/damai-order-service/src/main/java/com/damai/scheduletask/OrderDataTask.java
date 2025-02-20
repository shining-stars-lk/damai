package com.damai.scheduletask;

import com.damai.BusinessThreadPool;
import com.damai.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单服务定时任务重置
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class OrderDataTask {
    
    @Autowired
    private OrderService orderService;
    
    @Scheduled(cron = "0 0 23 * * ?")
    public void executeTask(){
        BusinessThreadPool.execute( () -> {
            try {
                log.warn("订单服务定时任务重置执行");
                orderService.delOrderAndOrderTicketUser();
            }catch (Exception e) {
                log.error("executeTask error",e);
            }
        });
    }
}
