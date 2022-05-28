package com.example.threadpool;


import com.example.threadpool.base.BaseThreadPoolMDC;
import com.example.threadpool.namefactory.BusinessNameThreadFactory;
import com.example.threadpool.rejectedexecutionhandler.ThreadPoolRejectedExecutionHandler;
import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: msa-toolkit
 * @description: 定制化线程池(此线程池能够获取服务调用链路requestId)
 * @author: lk
 * @create: 2021-12-11 14:09
 **/

public class BusinessThreadPool extends BaseThreadPoolMDC {
    private static ThreadPoolExecutor execute = null;

    static {
        execute = new CustomerThreadPoolExecutor(
                // 核心线程数
                Runtime.getRuntime().availableProcessors() + 1,
                // 最大线程数
                calcuteMaximumPoolSize(),
                // 线程存活时间
                60,
                // 存活时间单位
                TimeUnit.SECONDS,
                // 队列容量
                new ArrayBlockingQueue<>(300),
                // 线程工厂
                new BusinessNameThreadFactory(),
                // 拒绝策略
                new ThreadPoolRejectedExecutionHandler.BusinessAbortPolicy());
    }

    private static Integer calcuteMaximumPoolSize() {
        return new BigDecimal(Runtime.getRuntime().availableProcessors())
                .divide(new BigDecimal("0.2"), 0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    static class CustomerThreadPoolExecutor extends ThreadPoolExecutor {
        public CustomerThreadPoolExecutor(int corePoolSize,
                                          int maximumPoolSize,
                                          long keepAliveTime,
                                          TimeUnit unit,
                                          BlockingQueue<Runnable> workQueue,
                                          ThreadFactory threadFactory,
                                          RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }
    }

    /**
     * 执行任务
     *
     * @param r 提交的任务
     * @return
     */
    public static void execute(Runnable r) {
        execute.execute(wrapExecute(r, getContextForTask()));
    }

    /**
     * 执行带返回值任务
     *
     * @param c 提交的任务
     * @return
     */
    public static <T> Future<T> submit(Callable<T> c) {
        return execute.submit(wrapSubmit(c, getContextForTask()));
    }
}
