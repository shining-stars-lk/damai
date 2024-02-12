package com.example.util;

/**
 * @program: redis-example
 * @description: 有返回值的任务
 * @author: 星哥
 * @create: 2023-05-28
 **/
@FunctionalInterface
public interface TaskCall<V> {

    V call();
}
