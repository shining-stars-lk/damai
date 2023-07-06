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
public class ApiDataDto {
    
    @NotNull
    private Integer pageNo;
    
    @NotNull
    private Integer pageSize;
    
    private String startDate;
    
    private String endDate;
    
    private String apiAddress;
    
    private String apiUrl;
}
