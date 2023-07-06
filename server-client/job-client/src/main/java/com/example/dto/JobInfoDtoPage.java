package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-28
 **/
@Data
public class JobInfoDtoPage {
    
    @NotNull
    private Integer pageSize;
    
    @NotNull
    private Integer pageNo;
}
