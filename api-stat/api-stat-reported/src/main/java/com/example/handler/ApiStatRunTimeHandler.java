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
import java.math.RoundingMode;

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
        long start = System.nanoTime();
        MethodData parentMethodData = methodDataOperate.getParentMethodData();
        MethodHierarchyTransfer methodHierarchyTransfer = new MethodHierarchyTransfer();
        boolean exceptionFlag = false;
        try {
            obj = methodInvocation.proceed();
        } catch (Throwable t) {
            exceptionFlag = true;
            throw t;
        } finally {
            long runTime = System.nanoTime() - start;
            BigDecimal runTimeBigDecimal = new BigDecimal(String.valueOf(runTime)).divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP);
            MethodData currentMethodData = methodDataOperate.getCurrentMethodNode(methodInvocation, runTimeBigDecimal);
            methodDataStackHolder.putMethodData(currentMethodData);
            methodHierarchyTransfer = methodHierarchyTransferOperate.getMethodHierarchyTransfer(methodInvocation,parentMethodData,
                    currentMethodData,exceptionFlag);
            methodQueueOperate.add(methodHierarchyTransfer);
            methodDataStackHolder.remove();
        }
        return obj;
    }
}
