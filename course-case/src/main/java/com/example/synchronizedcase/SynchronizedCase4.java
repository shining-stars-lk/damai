package com.example.synchronizedcase;

/**
 * @program: toolkit
 * @description: 开启10个线程，每个线程中循环100次对result变量进行++自增，主线程等待10s后输出result值结果
 *               将synchronized添加在此class上，则锁的范围为此类，此范围大于实例对象范围
 * @author: lk
 * @create: 2023-04-23
 **/
public class SynchronizedCase4 {
    private static Integer result = 0;
    
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (SynchronizedCase4.class) {
                    for (int j = 0; j < 100; j++) {
                        result++;
                    }
                }
            }).start();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("执行结果 result : " + result);
    }
}
