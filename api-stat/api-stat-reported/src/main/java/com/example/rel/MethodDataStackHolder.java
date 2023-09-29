package com.example.rel;

import com.example.enums.MethodType;
import com.example.structure.MethodData;
import com.example.util.ApiStatCommon;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Stack;

import static com.example.rel.constant.ApiStatConstant.METHOD_DATA_SPLIT;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
public class MethodDataStackHolder {
    
    private final ThreadLocal<Stack<MethodData>> threadLocal;
    
    public MethodDataStackHolder(){
        threadLocal = new ThreadLocal<>();
    }
    
    public void putMethodData(MethodInvocation methodInvocation) {
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        String methodName = methodInvocation.getMethod().getName();
        Stack<MethodData> stack = null;
        if (threadLocal.get() == null) {
            stack = new Stack<>();
        }else {
            stack = threadLocal.get();
        }
        MethodType methodType = ApiStatCommon.getMethodType(methodInvocation);
        MethodData methodData = new MethodData();
        methodData.setId(className + METHOD_DATA_SPLIT + methodName);
        methodData.setMethodName(methodName);
        methodData.setClassName(className);
        methodData.setMethodType(methodType);
        stack.add(methodData);
        threadLocal.set(stack);
    }
    
    public Stack<MethodData> getMethodData() {
        return threadLocal.get();
    }
    
    public void clear() {
        Stack<MethodData> stack = threadLocal.get();
        if (stack==null) {
            return;
        }
        stack.pop();
        if (stack.isEmpty()) {
            threadLocal.remove();
        }
    }
}
