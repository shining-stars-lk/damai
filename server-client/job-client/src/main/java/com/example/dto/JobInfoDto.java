package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-28
 **/
@Data
public class JobInfoDto {
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotBlank
    private String url;
    
    private String headers;
    
    @NotNull
    private Integer method;
    
    private String params;
    
    private Integer retry;
    
    private Integer retryNumber;
}
