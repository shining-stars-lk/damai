package com.example.threadpool.namefactory;

/**
 * @program: msa-toolkit
 * @description: 业务线程工厂
 * @author: lk
 * @create: 2021-12-16 14:09
 **/
public class BusinessNameThreadFactory extends AbstractNameThreadFactory {

    /**
     * 将线程池工厂的前缀
     * 例子:task-pool--1(线程池的数量)
     */
    @Override
    public String getNamePrefix() {
        return "task-pool" + "--" + poolNum.getAndIncrement();
    }
}
