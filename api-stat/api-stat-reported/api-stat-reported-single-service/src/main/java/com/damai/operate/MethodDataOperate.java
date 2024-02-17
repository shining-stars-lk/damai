package com.damai.operate;

import com.damai.enums.MethodLevel;
import com.damai.MethodDataStackHolder;
import com.damai.structure.MethodData;
import com.damai.common.ApiStatCommon;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Stack;

import static com.damai.constant.ApiStatConstant.METHOD_DATA_SPLIT;

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
            return null;
        }
        return stack.peek();
    }
    
    public MethodData getCurrentMethodData(MethodInvocation methodInvocation) {
        String className = methodInvocation.getMethod().getDeclaringClass().getName();
        String methodName = methodInvocation.getMethod().getName();
        MethodData methodData = new MethodData();
        methodData.setId(apiStatCommon.getApplicationName() + METHOD_DATA_SPLIT + className + METHOD_DATA_SPLIT + methodName);
        methodData.setClassName(className);
        methodData.setMethodName(methodName);
        methodData.setMethodLevel(apiStatCommon.getMethodLevel(methodInvocation));
        if (methodData.getMethodLevel()== MethodLevel.CONTROLLER) {
            methodData.setApi(apiStatCommon.getApi(methodInvocation));
        }
        return methodData;
    }
}
