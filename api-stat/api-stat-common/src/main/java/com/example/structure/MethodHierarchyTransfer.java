package com.example.structure;

import lombok.Data;

import java.lang.reflect.Parameter;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@Data
public class MethodHierarchyTransfer {
    
    private MethodData currentMethodData;
    
    private MethodData parentMethodData;
    
    private boolean exceptionFlag;
    
    Parameter[] names;
    
    Object[] values;
}
