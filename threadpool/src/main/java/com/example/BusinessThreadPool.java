package com.example;


import com.example.base.BaseThreadPool;
import com.example.namefactory.BusinessNameThreadFactory;
import com.example.rejectedexecutionhandler.ThreadPoolRejectedExecutionHandler;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 
 * @author: 星哥
 * @create: 2023-06-06
 **/

public class BusinessThreadPool extends BaseThreadPool {
    private static ThreadPoolExecutor execute = null;

    static {
        execute = new ThreadPoolExecutor(
                
                Runtime.getRuntime().availableProcessors() + 1,
                
                maximumPoolSize(),
                
                60,
               
                TimeUnit.SECONDS,
                
                new ArrayBlockingQueue<>(600),
                
                new BusinessNameThreadFactory(),
                
                new ThreadPoolRejectedExecutionHandler.BusinessAbortPolicy());
    }

    private static Integer maximumPoolSize() {
        return new BigDecimal(Runtime.getRuntime().availableProcessors())
                .divide(new BigDecimal("0.2"), 0, BigDecimal.ROUND_HALF_UP).intValue();
    }
    
   
    public static void execute(Runnable r) {
        execute.execute(wrapTask(r, getContextForTask(), getContextForHold()));
    }

    
    public static <T> Future<T> submit(Callable<T> c) {
        return execute.submit(wrapTask(c, getContextForTask(), getContextForHold()));
    }
}
