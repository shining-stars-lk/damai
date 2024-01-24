package com.example.operate;

import java.util.concurrent.TimeUnit;

/**
 * @program: redis-example
 * @description: redisson操作接口
 * @author: 星哥
 * @create: 2023-05-28
 **/
public interface Operate {

    void set(String name,Object o);

    void set(String name,Object o,long timeToLive, TimeUnit timeUnit);

    Object get(String name);
}
