package com.example.service;


import com.example.model.MethodNode;
import com.example.util.Common;
import com.example.util.MethodStack;
import com.example.util.MethodType;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * zhangchang
 */
public class MethodNodeService {
    private static Logger log = Logger.getLogger(MethodNodeService.class.toString());

    public static MethodNode getParentMethodNode() {
        Stack<String> stack = MethodStack.get();
        if (null==stack) {
            MethodNode parent = new MethodNode();
            parent.setId("com.langpy.kotime.Controller.dispatch");
            parent.setClassName("Controller");
            parent.setMethodName("dispatch");
            parent.setName("Controller.dispatch");
            parent.setMethodType(MethodType.Dispatcher);
            return parent;
        }
        String classMethod = stack.peek();
        String[] classMethodSplit = classMethod.split("#");
        String parentClassName = classMethodSplit[0];
        String parentMothodName = classMethodSplit[1];
        String parentMothodType = classMethodSplit[2];
        MethodNode parent = new MethodNode();
        parent.setId(parentClassName + "." + parentMothodName);
        parent.setClassName(parentClassName);
        parent.setMethodName(parentMothodName);
        parent.setName(parentClassName.substring(parentClassName.lastIndexOf(".") + 1) + "." + parentMothodName);
        parent.setMethodType(Common.getMethodType(parentMothodType));
        return parent;
    }

    public static MethodNode getCurrentMethodNode(MethodInvocation pjp, double runTime) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        MethodNode current = new MethodNode();
        current.setName(className.substring(className.lastIndexOf(".") + 1) + "." + methodName);
        current.setId(className + "." + methodName);
        current.setClassName(className);
        current.setMethodName(methodName);
        BigDecimal bg = BigDecimal.valueOf(runTime);
        runTime = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        current.setValue(runTime);
        current.setMethodType(Common.getMethodType(pjp));
        if (current.getMethodType()==MethodType.Controller) {
            current.setRouteName(Common.getRoute(pjp));
        }
        return current;
    }
}
