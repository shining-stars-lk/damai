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
 * @description: 定制化线程池(此线程池能够获取服务调用链路traceId)
 * @author: kuan
 * @create: 2023-06-06 14:09
 **/

public class BusinessThreadPool extends BaseThreadPool {
    private static ThreadPoolExecutor execute = null;

    static {
        execute = new ThreadPoolExecutor(
                // 核心线程数
                Runtime.getRuntime().availableProcessors() + 1,
                // 最大线程数
                maximumPoolSize(),
                // 线程存活时间
                60,
                // 存活时间单位
                TimeUnit.SECONDS,
                // 队列容量
                new ArrayBlockingQueue<>(600),
                // 线程工厂
                new BusinessNameThreadFactory(),
                // 拒绝策略
                new ThreadPoolRejectedExecutionHandler.BusinessAbortPolicy());
    }

    private static Integer maximumPoolSize() {
        return new BigDecimal(Runtime.getRuntime().availableProcessors())
                .divide(new BigDecimal("0.2"), 0, BigDecimal.ROUND_HALF_UP).intValue();
    }
    
    /**
     * 执行任务
     *
     * @param r 提交的任务
     * @return
     */
    public static void execute(Runnable r) {
        execute.execute(wrapTask(r, getContextForTask(), getContextForHold()));
    }

    /**
     * 执行带返回值任务
     *
     * @param c 提交的任务
     * @return
     */
    public static <T> Future<T> submit(Callable<T> c) {
        return execute.submit(wrapTask(c, getContextForTask(), getContextForHold()));
    }
}
