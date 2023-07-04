package com.tool.servicelock.util;

/**
 * @program: redis-tool
 * @description: 无返回值的任务
 * @author: k
 * @create: 2022-05-28
 **/
@FunctionalInterface
public interface TaskRun {

    void run();
}
