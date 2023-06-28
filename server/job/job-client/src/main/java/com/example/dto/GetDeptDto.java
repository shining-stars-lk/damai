package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-05
 **/
@Data
public class GetDeptDto {
    
    @NotBlank
    private String typeCode;
}
