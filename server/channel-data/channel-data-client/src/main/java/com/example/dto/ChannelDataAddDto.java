package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-07-04
 **/
@Data
public class ChannelDataAddDto {
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String code;
    
    private String introduce;
    
    @NotBlank
    private String publicKey;
    
    @NotBlank
    private String secretKey;
    
    private String aesKey;
    
}
