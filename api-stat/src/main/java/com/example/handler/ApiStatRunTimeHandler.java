package com.example.handler;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-26
 **/
public class ApiStatRunTimeHandler implements MethodInterceptor {
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        boolean exceptionEnable = false;
        long begin = System.nanoTime();
        Object result = null;
        try {
            result = invocation.proceed();
            long end = System.nanoTime();
        }catch (Throwable e) {
            throw e;
        }finally {
            
        }
        return result;
    }
}
