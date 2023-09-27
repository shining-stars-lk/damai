package com.example.util;

import com.example.model.ApiStatMethodNode;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Stack;

/**
 * zhangchang
 */
public class MethodStackHolder {

    private static final ThreadLocal<Stack<ApiStatMethodNode>> threadLocal = new ThreadLocal<>();

    public static void putMethod(MethodInvocation methodInvocation) {
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        String methodName = methodInvocation.getMethod().getName();
        Stack<ApiStatMethodNode> stack = null;
        if (null==threadLocal.get()) {
            stack = new Stack<>();
        }else {
            stack = threadLocal.get();
        }
        MethodType methodType = ApiStatCommon.getMethodType(methodInvocation);
        ApiStatMethodNode apiStatMethodNode = new ApiStatMethodNode();
        apiStatMethodNode.setId(className + "." + methodName);
        apiStatMethodNode.setMethodName(methodName);
        apiStatMethodNode.setClassName(className);
        apiStatMethodNode.setName(className.substring(className.lastIndexOf(".") + 1) + "." + methodName);
        apiStatMethodNode.setMethodType(methodType);
        stack.add(apiStatMethodNode);
        threadLocal.set(stack);
    }

    public static Stack<ApiStatMethodNode> get() {
        return threadLocal.get();
    }

    public static void clear() {
        Stack<ApiStatMethodNode> queue = threadLocal.get();
        if (queue==null) {
            return;
        }
        queue.pop();
        if (queue.isEmpty()) {
            threadLocal.remove();
        }
    }
}
