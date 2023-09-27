package com.example.handler;

import com.example.config.ApiStatProperties;
import com.example.model.ApiStatInvokedInfo;
import com.example.model.ApiStatMethodNode;
import com.example.service.ApiStatInvokedQueue;
import com.example.service.ApiStatMethodNodeService;
import com.example.util.ApiStatCommon;
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
    
    @Override
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
        Object obj = null;
        long begin = System.nanoTime();
        ApiStatMethodNode parentMethodNode = ApiStatMethodNodeService.getParentMethodNode();
        MethodStackHolder.putMethod(methodInvocation);
        ApiStatInvokedInfo apiStatInvokedInfo = new ApiStatInvokedInfo();
        try {
            obj = methodInvocation.proceed();
            long end = System.nanoTime();
            apiStatInvokedInfo = ApiStatCommon.getApiStatInvokedInfo(methodInvocation, parentMethodNode, ((end - begin) / 1000000.0));
        } catch (Throwable t) {
            throw t;
        } finally {
            apiStatInvokedQueue.add(apiStatInvokedInfo);
        }
        return obj;
    }
}
