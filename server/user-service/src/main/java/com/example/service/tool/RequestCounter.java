package com.example.service.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RequestCounter {
    
    private final AtomicInteger count = new AtomicInteger(0);
    private final AtomicLong lastResetTime = new AtomicLong(System.currentTimeMillis());
    @Value("${request_count_threshold:100}")
    private int MAX_REQUESTS_PER_SECOND = 100;
    
    
    public synchronized boolean onRequest() {
        long currentTime = System.currentTimeMillis();
        // 如果当前时间和上次重置时间差超过1秒
        if (currentTime - lastResetTime.get() >= 1000) {
            count.set(0); // 重置计数器
            lastResetTime.set(currentTime); // 更新重置时间
        }
        
        if (count.incrementAndGet() > MAX_REQUESTS_PER_SECOND) {
            System.out.println("请求超过每秒100次限制");
            count.set(0); // 超过限制后重置计数器
            // 更新重置时间
            lastResetTime.set(System.currentTimeMillis());
            return true;
        }
        return false;
    }
}
