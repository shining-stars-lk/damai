package com.damai.service.test;

import com.alibaba.fastjson2.JSON;
import com.damai.context.DelayQueueContext;
import com.damai.core.ConsumerTask;
import com.damai.dto.TestSendDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: Test
 * @author: 阿星不是程序员
 **/
@Slf4j
@Service
public class Test implements ConsumerTask {
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    
    @Override
    public void execute(String content) {
        TestSendDto testSendDto = JSON.parseObject(content, TestSendDto.class);
        log.info("收到消息 : {} 延时: {} 毫秒" ,content,System.currentTimeMillis() - testSendDto.getTime() - 5000);
    }
    
    @Override
    public String topic() {
        return "test-topic";
    }
}
