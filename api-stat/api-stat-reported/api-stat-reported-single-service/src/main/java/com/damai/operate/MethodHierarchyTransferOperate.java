package com.damai.operate;

import com.damai.structure.MethodData;
import com.damai.structure.MethodHierarchyTransfer;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
public class MethodHierarchyTransferOperate {
    
    public MethodHierarchyTransfer getMethodHierarchyTransfer(MethodInvocation methodInvocation, MethodData parentMethodData,
                                                              MethodData currentMethodData, boolean exceptionFlag) {
        MethodHierarchyTransfer methodHierarchyTransfer = new MethodHierarchyTransfer();
        methodHierarchyTransfer.setCurrentMethodData(currentMethodData);
        methodHierarchyTransfer.setParentMethodData(parentMethodData);
        methodHierarchyTransfer.setNames(methodInvocation.getMethod().getParameters());
        methodHierarchyTransfer.setValues(methodInvocation.getArguments());
        methodHierarchyTransfer.setExceptionFlag(exceptionFlag);
        return methodHierarchyTransfer;
    }
}
