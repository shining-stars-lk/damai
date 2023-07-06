package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-28
 **/
@Data
public class RunJobDto {
    
    @NotBlank
    private String id;
}
