package com.example.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;

/**
 * @program: toolkit
 * @description: 讲解线程池的拒绝策略
 * @author: k
 * @create: 2023-06-08
 **/
public class Case2 {
    
    /**
     * 默认
     * 丢弃任务并抛出{@code RejectedExecutionException}异常
     * */
    private static RejectedExecutionHandler abortPolicy = new AbortPolicy();
    
    /**
     * 丢弃任务，但是不抛出异常。
     * 使用此策略，可能会使我们无法发现系统的异常状态
     * */
    private static RejectedExecutionHandler discardPolicy = new DiscardPolicy();
    
    /**
     * 丢弃队列最前面的任务，然后重新提交被拒绝的任务
     * */
    private static RejectedExecutionHandler discardOldestPolicy = new DiscardOldestPolicy();
    
    /**
     * 由调用线程(提交任务的线程)处理该任务
     * */
    private static RejectedExecutionHandler callerRunsPolicy = new CallerRunsPolicy();
    
    /**
     * int corePoolSize 核心线程数量
     * int maximumPoolSize 最大线程数
     * long keepAliveTime 超时时间,默认超出核心线程数量以外的线程空余存活时间
     * TimeUnit unit 存活时间单位
     * BlockingQueue<Runnable> workQueue 保存执行任务的队列
     * */
    private static ThreadPoolExecutor threadPool = 
            new ThreadPoolExecutor(
                    1,
                    2,
                    60,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1),
                    abortPolicy);
    
    public static void main(String[] args) {
        Runnable runnable1 = () -> {
            System.out.println("执行任务1开始，线程:" + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("执行任务1结束，线程:" + Thread.currentThread().getName());    
        };
        Runnable runnable2 = () -> {
            System.out.println("执行任务2开始，线程:" + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("执行任务2结束，线程:" + Thread.currentThread().getName());
        };
        Runnable runnable3 = () -> {
            System.out.println("执行任务3开始，线程:" + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("执行任务3结束，线程:" + Thread.currentThread().getName());
        };
        Runnable runnable4 = () -> {
            System.out.println("执行任务4开始，线程:" + Thread.currentThread().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("执行任务4结束，线程:" + Thread.currentThread().getName());
        };
        threadPool.execute(runnable1);
        threadPool.execute(runnable2);
        threadPool.execute(runnable3);
        threadPool.execute(runnable4);
        threadPool.shutdown();
    }
}
