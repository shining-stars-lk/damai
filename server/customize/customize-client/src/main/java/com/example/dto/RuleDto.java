package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Data
public class RuleDto {
    
    @NotNull
    private Integer statisticTime;
    @NotNull
    private Integer statisticTimeType;
    @NotNull
    private Integer thresholdValue;
    @NotNull
    private Integer limitTime;
    @NotNull
    private Integer limitTimeType;
    
    private String message;
}
