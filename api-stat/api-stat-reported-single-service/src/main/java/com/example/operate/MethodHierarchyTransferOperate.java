package com.example.operate;

import com.example.enums.MethodLevel;
import com.example.structure.MethodData;
import com.example.structure.MethodHierarchyTransfer;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;

import java.math.BigDecimal;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
public class MethodHierarchyTransferOperate {
    
    public MethodHierarchyTransfer getMethodHierarchyTransfer(MethodInvocation methodInvocation, MethodData parentMethodData,
                                                              MethodData currentMethodData, boolean exceptionFlag) {
        //parentMethodData = checkControllerParent(parentMethodData, currentMethodData);
        MethodHierarchyTransfer methodHierarchyTransfer = new MethodHierarchyTransfer();
        methodHierarchyTransfer.setCurrentMethodData(currentMethodData);
        methodHierarchyTransfer.setParentMethodData(parentMethodData);
        methodHierarchyTransfer.setNames(methodInvocation.getMethod().getParameters());
        methodHierarchyTransfer.setValues(methodInvocation.getArguments());
        methodHierarchyTransfer.setExceptionFlag(exceptionFlag);
        return methodHierarchyTransfer;
    }
    
    private MethodData checkControllerParent(MethodData parent, MethodData current) {
        if (current.getMethodLevel() == MethodLevel.Controller) {
            parent = new MethodData();
            parent.setId("org.springframework.web.servlet.DispatcherServlet.doDispatch");
            parent.setClassName("DispatcherServlet");
            parent.setMethodName("doDispatch");
            parent.setMethodLevel(MethodLevel.Servlet);
            return parent;
        }
        return parent;
    }
}
