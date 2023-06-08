package com.example.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: toolkit
 * @description: 讲解线程池的参数作用和运行流程
 * @author: k
 * @create: 2023-06-08
 **/
public class Case1 {
    
    /**
     * int corePoolSize 核心线程数量
     * int maximumPoolSize 最大线程数
     * long keepAliveTime 超时时间,默认超出核心线程数量以外的线程空余存活时间
     * TimeUnit unit 存活时间单位
     * BlockingQueue<Runnable> workQueue 保存执行任务的队列
     * */
    private static ThreadPoolExecutor threadPool = 
            new ThreadPoolExecutor(1,2,5,TimeUnit.SECONDS,new ArrayBlockingQueue<>(1));
    
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
        System.out.println("执行任务前，活跃线程数:" + threadPool.getActiveCount());
        threadPool.execute(runnable1);
        threadPool.execute(runnable2);
        threadPool.execute(runnable3);
        //threadPool.execute(runnable4);
        System.out.println("执行任务后，活跃线程数:" + threadPool.getActiveCount());
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("休眠6s后，活跃线程数:" + threadPool.getActiveCount());
        threadPool.shutdown();
    }
}
