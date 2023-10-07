package com.example;

import com.example.structure.MethodData;

import java.util.Stack;

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
    
    public void putMethodData(MethodData methodData) {
        Stack<MethodData> stack = null;
        if (threadLocal.get() == null) {
            stack = new Stack<>();
        }else {
            stack = threadLocal.get();
        }
        stack.add(methodData);
        threadLocal.set(stack);
    }
    
    public Stack<MethodData> getMethodData() {
        return threadLocal.get();
    }
    
    public void remove() {
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
