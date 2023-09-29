package com.example.service;


import com.example.enums.MethodType;
import com.example.model.ApiStatMethodNode;
import com.example.util.ApiStatCommon;
import com.example.util.MethodStackHolder;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

@Slf4j
public class ApiStatMethodNodeService {

    public static ApiStatMethodNode getParentMethodNode() {
        Stack<ApiStatMethodNode> stack = MethodStackHolder.get();
        if (null==stack) {
            ApiStatMethodNode apiStatMethodNode = new ApiStatMethodNode();
            apiStatMethodNode.setId("org.springframework.web.servlet.DispatcherServlet.doDispatch");
            apiStatMethodNode.setClassName("DispatcherServlet");
            apiStatMethodNode.setMethodName("doDispatch");
            apiStatMethodNode.setName("DispatcherServlet.doDispatch");
            apiStatMethodNode.setMethodType(MethodType.Dispatcher);
            return apiStatMethodNode;
        }
        ApiStatMethodNode apiStatMethodNode = stack.peek();
        return apiStatMethodNode;
    }

    public static ApiStatMethodNode getCurrentMethodNode(MethodInvocation pjp, double runTime) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        ApiStatMethodNode apiStatMethodNode = new ApiStatMethodNode();
        apiStatMethodNode.setName(className.substring(className.lastIndexOf(".") + 1) + "." + methodName);
        apiStatMethodNode.setId(className + "." + methodName);
        apiStatMethodNode.setClassName(className);
        apiStatMethodNode.setMethodName(methodName);
        BigDecimal bg = BigDecimal.valueOf(runTime);
        runTime = bg.setScale(2, RoundingMode.HALF_UP).doubleValue();
        apiStatMethodNode.setValue(runTime);
        apiStatMethodNode.setMethodType(ApiStatCommon.getMethodType(pjp));
        if (apiStatMethodNode.getMethodType()==MethodType.Controller) {
            apiStatMethodNode.setRouteName(ApiStatCommon.getRoute(pjp));
        }
        return apiStatMethodNode;
    }
}
