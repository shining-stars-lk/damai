package com.damai.handler;

import com.damai.MethodDataStackHolder;
import com.damai.operate.MethodDataOperate;
import com.damai.operate.MethodHierarchyTransferOperate;
import com.damai.operate.MethodQueueOperate;
import com.damai.structure.MethodData;
import com.damai.structure.MethodHierarchyTransfer;

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
        Object obj;
        long start = System.currentTimeMillis();
        MethodData parentMethodData = methodDataOperate.getParentMethodData();
        MethodHierarchyTransfer methodHierarchyTransfer;
        boolean exceptionFlag = false;
        MethodData currentMethodData;
        currentMethodData = methodDataOperate.getCurrentMethodData(methodInvocation);
        try {
            methodDataStackHolder.putMethodData(currentMethodData);
            obj = methodInvocation.proceed();
        } catch (Throwable t) {
            exceptionFlag = true;
            throw t;
        } finally {
            long end = System.currentTimeMillis();
            currentMethodData.setExecuteTime(new BigDecimal(end - start));
            methodHierarchyTransfer = methodHierarchyTransferOperate.getMethodHierarchyTransfer(methodInvocation,parentMethodData,
                    currentMethodData,exceptionFlag);
            methodQueueOperate.add(methodHierarchyTransfer);
            methodDataStackHolder.remove();
        }
        return obj;
    }
}
