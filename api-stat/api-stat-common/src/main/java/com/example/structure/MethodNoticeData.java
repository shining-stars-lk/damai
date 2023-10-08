package com.example.structure;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-08
 **/
@Data
public class MethodNoticeData {
    
    private Integer argumentCount;
    
    private String api;
    
    private BigDecimal avgExecuteTime = new BigDecimal("0");
}
