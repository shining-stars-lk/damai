package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class PayDto {
    
    private Long id;
    
    private String userId;
    
    private Integer type;
    
    private Integer status;
    
    private BigDecimal amount;
    
    private Integer origin;
    
    private String payTime;
    
    private String departmentId;
    
    private String createTime;
    
    private Integer refundStatus;
    
    private BigDecimal refundAmount;
    
    private String refundTime;
    
    private String refundMark;
    
    private List<String> departmentIdList;
}