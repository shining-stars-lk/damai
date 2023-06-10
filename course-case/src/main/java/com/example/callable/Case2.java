package com.example.callable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @program: toolkit
 * @description: 演示callable的使用和特点
 * @author: k
 * @create: 2023-06-09
 **/
public class Case2 {

    public static void main(String[] args) {
        FutureTask task = new FutureTask(() -> {
            System.out.println("执行call方法开始");
            Thread.sleep(3000);
            int i = 1 / 0;
            System.out.println("执行call方法结束");
            return 1;
        });
        new Thread(task).start();
        System.out.println("执行main线程任务");
        try {
            System.out.println("获取call方法结果:"+task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
