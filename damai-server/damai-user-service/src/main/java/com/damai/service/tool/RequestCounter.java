package com.damai.service.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 计数器
 * @author: 阿宽不是程序员
 **/
@Component
public class RequestCounter {
    
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());
    @Value("${request_count_threshold:100}")
    private int maxRequestsPerSecond = 100;
    
    public synchronized boolean onRequest() {
        long currentTime = System.currentTimeMillis();
        // 如果当前时间和上次重置时间差超过1秒
        long differenceValue = 1000;
        if (currentTime - lastResetTime.get() >= differenceValue) {
            // 重置计数器
            count.set(0);
            // 更新重置时间
            lastResetTime.set(currentTime);
        }
        
        if (count.incrementAndGet() > maxRequestsPerSecond) {
            System.out.println("请求超过每秒100次限制");
            // 超过限制后重置计数器
            count.set(0);
            // 更新重置时间
            lastResetTime.set(System.currentTimeMillis());
            return true;
        }
        return false;
    }
}
