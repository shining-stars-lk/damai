package com.example.rel.structure;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-09-28
 **/
@Data
public class MethodHierarchy {

    private String id;
    
    private String parentMethodDataId;
    
    private String currentMethodDataId;
    
    private BigDecimal avgExecuteTime = new BigDecimal("0");
    
    private BigDecimal maxExecuteTime = new BigDecimal("0");
    
    private BigDecimal minExecuteTime = new BigDecimal("0");
    
    private Long exceptionCount = 0L;
}
