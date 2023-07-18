package com.example.threadException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: cook-frame
 * @description: 讲解线程异常
 * @author: 星哥
 * @create: 2023-06-13
 **/
public class Case1 {
    
    public static void main(String[] args) {
        testThreadPoolSubmitException();
    }
    
    /**
     * 线程异常(默认处理)
     * */
    public static void testThreadException(){
        new Thread(() -> {
            System.out.println("===线程执行===");
            throw new RuntimeException("出现异常");
        }).start();
    }
    
    /**
     * 线程异常(实现UncaughtExceptionHandler自定义处理)
     * */
    public static void testUncaughtExceptionHandler(){
        Thread t = new Thread(() -> {
            System.out.println("===线程执行===");
            throw new RuntimeException("出现异常");
        });
        t.setName("test线程");
        t.setUncaughtExceptionHandler((t1, e) -> System.out.println(t1.getName()+"执行, 异常信息:"+e.getMessage()));
        t.start();
    }
    
    /**
     * 线程异常(线程池的线程工厂实现UncaughtExceptionHandler自定义处理)
     * */
    public static void testThreadPoolUncaughtExceptionHandler(){
        Thread thread = new Thread(() -> {
            System.out.println("===线程执行===");
            throw new RuntimeException("出现异常");
        });
        threadPool.execute(thread);
    }
    
    /**
     * 线程池的submit出现异常
     * */
    public static void testThreadPoolSubmitException(){
        executorService.execute(() -> test("execute"));
        //执行发现submit中的线程抛出异常后，主线程是看不到的
        Future<?> future = executorService.submit(() -> test("submit"));
        
        //submit方式执行，如果出现异常，只能在future.get()的显式捕获异常中获得到
        try {
            System.out.println(future.get());
        } catch (InterruptedException e) {
            System.out.println("异常信息:"+e.getMessage());
        } catch (ExecutionException e) {
            System.out.println("异常信息:"+e.getMessage());
        }
    }
    
    private static void test(String name) {
        System.out.println( "执行 : "+ name +" --- (线程名字:" + Thread.currentThread().getName() + ")");
        throw new RuntimeException("执行 : "+ name + " --- 出现异常");
    }
    
    private static ThreadPoolExecutor threadPool =
            new ThreadPoolExecutor(
                    1,
                    2,
                    5,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(1),
                    r -> {
                        Thread thread = new Thread(r);
                        thread.setUncaughtExceptionHandler((t1, e) -> System.out.println(t1.getName()+"执行, 异常信息:"+e.getMessage()));
                        return thread;
                    });
    
    
    
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
}
