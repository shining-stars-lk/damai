package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Data
public class RuleUpdateDto {
    
    @NotBlank
    private String id;
    
    private Integer statisticTime;
    
    private Integer statisticTimeType;
    
    private Integer thresholdValue;
    
    private Integer limitTime;
    
    private Integer limitTimeType;
    
    private Integer message;
    
    private Integer status;
    
    
}
