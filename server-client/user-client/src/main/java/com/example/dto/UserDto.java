package com.example.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserDto {

    private String name;
    
    @NotBlank
    private String mobile;
    
    @NotBlank
    private String code;

    private Date createTime;
}