package com.damai.service;

import com.alibaba.fastjson2.JSON;
import com.baidu.fsg.uid.utils.PaddedAtomicLong;
import com.damai.context.DelayQueueContext;
import com.damai.dto.TestSendDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: Test service
 * @author: 阿星不是程序员
 **/
@Slf4j
@Service
public class TestService {
    
    AtomicLong count = new PaddedAtomicLong(0);
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    public boolean testSend(TestSendDto testSendDto) {
        try {
            testSendDto.setTime(System.currentTimeMillis());
            testSendDto.setCount(count.incrementAndGet());
            String message = JSON.toJSONString(testSendDto);
            delayQueueContext.sendMessage("test-topic",
                    message, 5000, TimeUnit.MILLISECONDS);
            log.info("发送消息 : {}",message);
        }catch (Exception e) {
            log.error("test send message error message : {}",JSON.toJSONString(testSendDto),e);
            return false;
        }
        return true;
    }
    
    public Boolean reset(final TestSendDto testSendDto) {
        count.set(0);
        return true;
    }
}
