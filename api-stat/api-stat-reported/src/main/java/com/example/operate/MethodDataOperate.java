package com.example.operate;

import com.example.enums.MethodLevel;
import com.example.MethodDataStackHolder;
import com.example.structure.MethodData;
import com.example.util.ApiStatCommon;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;
import java.util.Stack;

import static com.example.constant.ApiStatConstant.METHOD_DATA_SPLIT;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@AllArgsConstructor
public class MethodDataOperate {
    
    private final MethodDataStackHolder methodDataStackHolder;
    
    
    public MethodData getParentMethodData() {
        Stack<MethodData> stack = methodDataStackHolder.getMethodData();
        if (stack == null) {
            MethodData methodData = new MethodData();
            methodData.setId("org.springframework.web.servlet.DispatcherServlet.doDispatch");
            methodData.setClassName("DispatcherServlet");
            methodData.setMethodName("doDispatch");
            methodData.setMethodLevel(MethodLevel.Servlet);
            return methodData;
        }
        return stack.peek();
    }
    
    public MethodData getCurrentMethodNode(MethodInvocation pjp, BigDecimal runTime) {
        String className = pjp.getMethod().getDeclaringClass().getName();
        String methodName = pjp.getMethod().getName();
        MethodData methodData = new MethodData();
        methodData.setId(className + METHOD_DATA_SPLIT + methodName);
        methodData.setClassName(className);
        methodData.setMethodName(methodName);
        methodData.setRunTime(runTime);
        methodData.setMethodLevel(ApiStatCommon.getMethodLevel(pjp));
        if (methodData.getMethodLevel()== MethodLevel.Controller) {
            methodData.setApi(ApiStatCommon.getRoute(pjp));
        }
        return methodData;
    }
}
