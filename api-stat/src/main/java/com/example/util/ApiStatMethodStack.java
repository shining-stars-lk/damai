package com.example.util;

import org.aopalliance.intercept.MethodInvocation;

import java.util.Stack;

/**
 * zhangchang
 */
public class ApiStatMethodStack {

    private static final ThreadLocal<Stack> threadMethods = new ThreadLocal<>();

    public static void record(MethodInvocation pjp) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        Stack<String> queue = null;
        if (null==threadMethods.get()) {
            queue = new Stack<>();
        }else {
             queue = threadMethods.get();
        }
        ApiStatMethodType methodType = ApiStatCommon.getMethodType(pjp);
        queue.add(className+"#"+methodName+"#"+methodType);
        threadMethods.set(queue);
    }

    public static Stack get() {
        return threadMethods.get();
    }

    public static void clear() {
        Stack<String> queue = threadMethods.get();
        if (queue==null) {
            return;
        }
        queue.pop();
        if (queue.isEmpty()) {
            threadMethods.remove();
        }
    }
}
