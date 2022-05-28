package com.example.distributecache.operate;

import java.util.concurrent.TimeUnit;

/**
 * @program: distribute-cache
 * @description: redisson操作接口
 * @author: lk
 * @create: 2022-05-28
 **/
public interface Operate {

    void set(String name,Object o);

    void set(String name,Object o,long timeToLive, TimeUnit timeUnit);

    Object get(String name);
}
