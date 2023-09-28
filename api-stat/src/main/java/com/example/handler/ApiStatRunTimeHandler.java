package com.example.handler;

import com.example.config.ApiStatProperties;
import com.example.rel.MethodDataStackHolder;
import com.example.rel.operate.MethodDataOperate;
import com.example.rel.operate.MethodHierarchyTransferOperate;
import com.example.rel.operate.MethodQueueOperate;
import com.example.rel.structure.MethodData;
import com.example.rel.structure.MethodHierarchyTransfer;
import com.example.service.ApiStatInvokedQueue;
import com.example.util.MethodStackHolder;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-27
 **/
@AllArgsConstructor
public class ApiStatRunTimeHandler implements MethodInterceptor {
    
    private final ApiStatProperties apiStatProperties;
    
    private final ApiStatInvokedQueue apiStatInvokedQueue;
    
    private final MethodDataOperate methodDataOperate;
    
    private final MethodDataStackHolder methodDataStackHolder;
    
    private final MethodHierarchyTransferOperate methodHierarchyTransferOperate;
    
    private final MethodQueueOperate methodQueueOperate;
    
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        Object obj = null;
        long begin = System.nanoTime();
        //ApiStatMethodNode parentMethodNode = ApiStatMethodNodeService.getParentMethodNode();
        MethodData parentMethodData = methodDataOperate.getParentMethodData();
        //MethodStackHolder.putMethod(methodInvocation);
        methodDataStackHolder.putMethodData(methodInvocation);
        //ApiStatInvokedInfo apiStatInvokedInfo = new ApiStatInvokedInfo();
        MethodHierarchyTransfer methodHierarchyTransfer = new MethodHierarchyTransfer();
        boolean exceptionFlag = false;
        try {
            obj = methodInvocation.proceed();
        } catch (Throwable t) {
            exceptionFlag = true;
            throw t;
        } finally {
            long end = System.nanoTime();
            //apiStatInvokedInfo = ApiStatCommon.getApiStatInvokedInfo(methodInvocation, parentMethodNode, ((end - begin) / 1000000.0),exceptionFlag);
            methodHierarchyTransfer = methodHierarchyTransferOperate.getMethodHierarchyTransfer(methodInvocation,parentMethodData,((end - begin) / 1000000.0),exceptionFlag);
            //apiStatInvokedQueue.add(apiStatInvokedInfo);
            methodQueueOperate.add(methodHierarchyTransfer);
            MethodStackHolder.clear();
        }
        return obj;
    }
}
