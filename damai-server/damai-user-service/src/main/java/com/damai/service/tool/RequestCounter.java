package com.damai.service.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 计数器
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class RequestCounter {
    
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());
    @Value("${request_count_threshold:1000}")
    private int maxRequestsPerSecond = 1000;
    
    public synchronized boolean onRequest() {
        long currentTime = System.currentTimeMillis();
        long differenceValue = 1000;
        if (currentTime - lastResetTime.get() >= differenceValue) {
            count.set(0);
            lastResetTime.set(currentTime);
        }
        
        if (count.incrementAndGet() > maxRequestsPerSecond) {
            log.warn("请求超过每秒{}次限制",maxRequestsPerSecond);
            count.set(0);
            lastResetTime.set(System.currentTimeMillis());
            return true;
        }
        return false;
    }
}
