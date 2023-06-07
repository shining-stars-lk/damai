package com.tool.servicelock.util;

/**
 * @program: distribute-cache
 * @description: 无返回值的任务
 * @author: k
 * @create: 2022-05-28
 **/
@FunctionalInterface
public interface TaskRun {

    void run();
}
