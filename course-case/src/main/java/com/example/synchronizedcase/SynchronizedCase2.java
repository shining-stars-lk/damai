package com.example.synchronizedcase;

/**
 * @program: toolkit
 * @description: 开启10个线程，每个线程中循环100次对result变量进行++自增，主线程等待10s后输出result值结果
 *               创建一个object对象，将synchronized添加在此object对象
 * @author: k
 * @create: 2023-04-23
 **/
public class SynchronizedCase2 {
    private static Integer result = 0;
    
    public static void main(String[] args) {
        Object object = new Object();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                synchronized (object) {
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
