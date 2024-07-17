package com.damai.pro.limit;

import com.damai.enums.BaseCode;
import com.damai.exception.DaMaiFrameException;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 线上限流工具
 * @author: 阿星不是程序员
 **/
public class RateLimiter {
    
    private final Semaphore semaphore;
    private final TimeUnit timeUnit;
    
    public RateLimiter(int maxPermitsPerSecond) {
        this.timeUnit = TimeUnit.SECONDS;
        this.semaphore = new Semaphore(maxPermitsPerSecond);
    }
    
    public void acquire() throws InterruptedException {
        if (!semaphore.tryAcquire(1, timeUnit)) {
            throw new DaMaiFrameException(BaseCode.OPERATION_IS_TOO_FREQUENT_PLEASE_TRY_AGAIN_LATER);
        }
    }
    
    public void release() {
        semaphore.release();
    }
}
