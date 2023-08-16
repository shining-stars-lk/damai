package com.example.thread;

/**
 * @program: apache-skywalking-java-agent-8.14.0
 * @description:
 * @author: k
 * @create: 2023-08-16
 **/
public class TestThreadV2 implements Runnable{
    @Override
    public void run() {
        System.out.println("任务执行");
    }
}
