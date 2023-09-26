package com.example.service;

import com.example.model.ApiStatMethodNode;
import com.example.util.ApiStatCommon;
import com.example.util.ApiStatMethodStack;
import com.example.util.ApiStatMethodType;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;
import java.util.Stack;

@Slf4j
public class ApiStatMethodNodeService {

    public static ApiStatMethodNode getParentMethodNode() {
        Stack<String> stack = ApiStatMethodStack.get();
        if (null==stack) {
            ApiStatMethodNode parent = new ApiStatMethodNode();
            parent.setId("com.langpy.kotime.Controller.dispatch");
            parent.setClassName("Controller");
            parent.setMethodName("dispatch");
            parent.setName("Controller.dispatch");
            parent.setApiStatMethodType(ApiStatMethodType.Dispatcher);
            return parent;
        }
        String classMethod = stack.peek();
        String[] classMethodSplit = classMethod.split("#");
        String parentClassName = classMethodSplit[0];
        String parentMothodName = classMethodSplit[1];
        String parentMothodType = classMethodSplit[2];
        ApiStatMethodNode parent = new ApiStatMethodNode();
        parent.setId(parentClassName + "." + parentMothodName);
        parent.setClassName(parentClassName);
        parent.setMethodName(parentMothodName);
        parent.setName(parentClassName.substring(parentClassName.lastIndexOf(".") + 1) + "." + parentMothodName);
        parent.setApiStatMethodType(ApiStatCommon.getMethodType(parentMothodType));
        return parent;
    }

    public static ApiStatMethodNode getCurrentMethodNode(MethodInvocation pjp, double runTime) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        ApiStatMethodNode current = new ApiStatMethodNode();
        current.setName(className.substring(className.lastIndexOf(".") + 1) + "." + methodName);
        current.setId(className + "." + methodName);
        current.setClassName(className);
        current.setMethodName(methodName);
        BigDecimal bg = BigDecimal.valueOf(runTime);
        runTime = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        current.setValue(runTime);
        current.setApiStatMethodType(ApiStatCommon.getMethodType(pjp));
        if (current.getApiStatMethodType()==ApiStatMethodType.Controller) {
            current.setRouteName(ApiStatCommon.getRoute(pjp));
        }
        return current;
    }
}
