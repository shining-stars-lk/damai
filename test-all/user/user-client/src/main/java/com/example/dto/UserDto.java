package com.example.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String name;
    
    private String mobile;

    private Date createTime;
}