package com.damai.lockinfo;

import org.aspectj.lang.JoinPoint;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 锁信息抽象
 * @author: 阿星不是程序员
 **/
public interface LockInfoHandle {
    /**
     * 获取锁信息
     * @param joinPoint 切面
     * @param name 锁业务名
     * @param keys 锁
     * @return 锁信息
     * */
    String getLockName(JoinPoint joinPoint, String name, String[] keys);
    
    /**
     * 拼装锁信息
     * @param name 锁业务名
     * @param keys 锁
     * @return 锁信息
     * */
    String simpleGetLockName(String name,String[] keys);
}
