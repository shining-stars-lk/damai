package com.example.rel.structure;

import com.example.util.MethodType;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@Data
public class MethodData {
    
    private String id;
    
    private String className;
    
    private String methodName;
    
    private Integer argumentCount;
    
    private MethodType methodType;
    
    private String api;
}
