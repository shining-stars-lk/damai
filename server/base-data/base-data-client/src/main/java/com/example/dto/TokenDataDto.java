package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-05
 **/
@Data
public class TokenDataDto {
    
    @NotBlank
    private String name;
    @NotBlank
    private String secret;
}
