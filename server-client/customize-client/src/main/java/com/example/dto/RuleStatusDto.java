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
public class RuleStatusDto {
    
    @NotBlank
    private String id;
    @NotNull
    private Integer status;
}
