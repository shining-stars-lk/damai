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
    private Integer statTime;
    @NotNull
    private Integer statTimeType;
    @NotNull
    private Integer threshold;
    @NotNull
    private Integer effectiveTime;
    @NotNull
    private Integer effectiveTimeType;
    @NotNull
    private String limitApi;
    
    private String message;
}
