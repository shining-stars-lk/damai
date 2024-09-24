package com.damai.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: damai
 * @description:
 * @author: k
 * @create: 2024-09-03
 **/
public class Test {
    
    private final AtomicInteger listenStartThreadCount = new AtomicInteger(1);
    
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2,
            4,
            30, 
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            r -> new Thread(Thread.currentThread().getThreadGroup(), r,
                    "test-thread-" + listenStartThreadCount.getAndIncrement())
            );
    
    public void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
    
    public static void main(String[] args) {
        Test test = new Test();
        test.execute(() -> System.out.println("执行任务，当前线程名:"+Thread.currentThread().getName()));
    }
}
