package com.example.structure;


import com.example.enums.MethodType;
import lombok.Data;

import java.math.BigDecimal;

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
    
    private BigDecimal runTime;
    
    private String api;
}
