package com.example.lockinfo;

import org.aspectj.lang.JoinPoint;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-07
 **/
public interface LockInfoHandle {
    
    String getLockName(JoinPoint joinPoint, String name, String[] keys);
    
    String simpleGetLockName(String name,String[] keys);
}
