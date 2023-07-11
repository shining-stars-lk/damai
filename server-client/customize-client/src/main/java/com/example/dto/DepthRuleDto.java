package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Data
public class DepthRuleDto {
    
    @NotBlank
    private String startTimeWindow;
    @NotBlank
    private String endTimeWindow;
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
    @NotBlank
    private String limitApi;
    
    private String message;
    
    private Integer status;
}
