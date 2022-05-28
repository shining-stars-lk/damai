package com.example.distributecache.distributedlock.util;

/**
 * @program: distribute-cache
 * @description: 有返回值的任务
 * @author: lk
 * @create: 2022-05-28
 **/
@FunctionalInterface
public interface TaskCall<V> {

    V call();
}
