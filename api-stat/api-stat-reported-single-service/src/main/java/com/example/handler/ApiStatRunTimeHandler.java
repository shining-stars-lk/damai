package com.example.handler;

import com.example.MethodDataStackHolder;
import com.example.operate.MethodDataOperate;
import com.example.operate.MethodHierarchyTransferOperate;
import com.example.operate.MethodQueueOperate;
import com.example.structure.MethodData;
import com.example.structure.MethodHierarchyTransfer;

import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@AllArgsConstructor
public class ApiStatRunTimeHandler implements MethodInterceptor {
    
    private final MethodDataOperate methodDataOperate;
    
    private final MethodDataStackHolder methodDataStackHolder;
    
    private final MethodHierarchyTransferOperate methodHierarchyTransferOperate;
    
    private final MethodQueueOperate methodQueueOperate;
    
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        Object obj = null;
        long start = System.currentTimeMillis();
        MethodData parentMethodData = methodDataOperate.getParentMethodData();
        MethodHierarchyTransfer methodHierarchyTransfer = new MethodHierarchyTransfer();
        boolean exceptionFlag = false;
        MethodData currentMethodData = null;
        try {
            currentMethodData = methodDataOperate.getCurrentMethodData(methodInvocation);
            methodDataStackHolder.putMethodData(currentMethodData);
            obj = methodInvocation.proceed();
        } catch (Throwable t) {
            exceptionFlag = true;
            throw t;
        } finally {
            currentMethodData.setExecuteTime(new BigDecimal(String.valueOf(System.currentTimeMillis() - start)));
            methodHierarchyTransfer = methodHierarchyTransferOperate.getMethodHierarchyTransfer(methodInvocation,parentMethodData,
                    currentMethodData,exceptionFlag);
            methodQueueOperate.add(methodHierarchyTransfer);
            methodDataStackHolder.remove();
        }
        return obj;
    }
}
