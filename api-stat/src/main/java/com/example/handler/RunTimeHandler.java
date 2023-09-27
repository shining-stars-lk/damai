package com.example.handler;


import com.example.model.InvokedInfo;
import com.example.model.MethodNode;
import com.example.service.InvokedQueue;
import com.example.service.MethodNodeService;
import com.example.util.Common;
import com.example.util.Context;
import com.example.util.MethodStack;
import com.example.util.ThrowException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * zhangchang
 */
public class RunTimeHandler implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        boolean kotimeEnable = Context.getConfig().getEnable();
        if (!kotimeEnable) {
            return invocation.proceed();
        }
        boolean exceptionEnable = Context.getConfig().getExceptionEnable();
        long begin = System.nanoTime();
        Object obj = null;
        MethodNode parent = MethodNodeService.getParentMethodNode();
        MethodStack.record(invocation);
        InvokedInfo invokedInfo = new InvokedInfo();
        try {
            obj = invocation.proceed();
            long end = System.nanoTime();
            invokedInfo = Common.getInvokedInfo(invocation, parent, ((end - begin) / 1000000.0));
        } catch (Exception te) {
            if (!exceptionEnable) {
                long end = System.nanoTime();
                invokedInfo = Common.getInvokedInfo(invocation, parent, ((end - begin) / 1000000.0));
                throw te;
            }
            Exception e = null;
            if (te instanceof ThrowException) {
                e = ((ThrowException) te).getOriginalException();
            } else {
                e = te;
            }
            long end = System.nanoTime();
            invokedInfo = Common.getInvokedInfoWithException(invocation, parent, e, ((end - begin) / 1000000.0));
            throw te;
        } finally {
            InvokedQueue.add(invokedInfo);
            InvokedQueue.wake();
            MethodStack.clear();
        }
        return obj;
    }
}
