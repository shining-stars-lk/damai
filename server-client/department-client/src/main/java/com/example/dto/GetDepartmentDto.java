package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Data
public class GetDepartmentDto {
    @NotBlank
    private String id;
    private String name;
    private Long sleepTime;
}
