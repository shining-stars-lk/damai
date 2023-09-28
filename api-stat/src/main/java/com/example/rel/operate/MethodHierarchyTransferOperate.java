package com.example.rel.operate;

import com.example.rel.structure.MethodData;
import com.example.rel.structure.MethodHierarchyTransfer;
import com.example.util.MethodType;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@AllArgsConstructor
public class MethodHierarchyTransferOperate {
    
    private final MethodDataOperate methodDataOperate;
    
    public MethodHierarchyTransfer getMethodHierarchyTransfer(MethodInvocation methodInvocation, MethodData parentMethodData, double runTime, boolean exceptionFlag) {
        MethodData currentMethodData = methodDataOperate.getCurrentMethodNode(methodInvocation, runTime);
        parentMethodData = checkControllerParent(parentMethodData, currentMethodData);
        MethodHierarchyTransfer methodHierarchyTransfer = new MethodHierarchyTransfer();
        methodHierarchyTransfer.setCurrentMethodData(currentMethodData);
        methodHierarchyTransfer.setParentMethodData(parentMethodData);
        methodHierarchyTransfer.setNames(methodInvocation.getMethod().getParameters());
        methodHierarchyTransfer.setValues(methodInvocation.getArguments());
        methodHierarchyTransfer.setExceptionFlag(exceptionFlag);
        return methodHierarchyTransfer;
    }
    
    private MethodData checkControllerParent(MethodData parent, MethodData current) {
        if (current.getMethodType() == MethodType.Controller) {
            parent = new MethodData();
            parent.setId("org.springframework.web.servlet.DispatcherServlet.doDispatch");
            parent.setClassName("DispatcherServlet");
            parent.setMethodName("doDispatch");
            parent.setMethodType(MethodType.Dispatcher);
            return parent;
        }
        return parent;
    }
}
