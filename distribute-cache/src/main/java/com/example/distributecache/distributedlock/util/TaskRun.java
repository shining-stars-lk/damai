package com.example.distributecache.distributedlock.util;

/**
 * @program: distribute-cache
 * @description: 无返回值的任务
 * @author: lk
 * @create: 2022-05-28
 **/
@FunctionalInterface
public interface TaskRun {

    void run();
}
