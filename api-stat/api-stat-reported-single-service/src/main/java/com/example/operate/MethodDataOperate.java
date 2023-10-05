package com.example.operate;

import com.example.enums.MethodLevel;
import com.example.MethodDataStackHolder;
import com.example.structure.MethodData;
import com.example.common.ApiStatCommon;
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

    private final ApiStatCommon apiStatCommon;
    
    
    public MethodData getParentMethodData() {
        Stack<MethodData> stack = methodDataStackHolder.getMethodData();
        if (stack == null) {
//            MethodData methodData = new MethodData();
//            methodData.setId("org.springframework.web.servlet.DispatcherServlet.doDispatch");
//            methodData.setClassName("DispatcherServlet");
//            methodData.setMethodName("doDispatch");
//            methodData.setMethodLevel(MethodLevel.Servlet);
//            return methodData;
            return null;
        }
        return stack.peek();
    }
    
    public MethodData getCurrentMethodData(MethodInvocation methodInvocation, BigDecimal executeTime) {
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        String methodName = methodInvocation.getMethod().getName();
        MethodData methodData = new MethodData();
        methodData.setId(className + METHOD_DATA_SPLIT + methodName);
        methodData.setClassName(className);
        methodData.setMethodName(methodName);
        methodData.setExecuteTime(executeTime);
        methodData.setMethodLevel(apiStatCommon.getMethodLevel(methodInvocation));
        if (methodData.getMethodLevel()== MethodLevel.Controller) {
            methodData.setApi(apiStatCommon.getApi(methodInvocation));
        }
        return methodData;
    }
}
