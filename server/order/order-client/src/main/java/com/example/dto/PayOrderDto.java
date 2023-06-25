package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-25
 **/
@Data
public class PayOrderDto {
    
    @NotNull
    private String id;
    
    @NotNull
    private Integer payChannelType;
}
