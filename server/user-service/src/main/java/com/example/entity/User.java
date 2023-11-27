package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/

@Data
public class User {
    
    private Long id;
    
    private String name;
    
    private String password;
    
    private Integer age;
    
    private Integer status;
    
    private Date createTime;
    
    private String mobile;
    
    private Date editTime;
    
    private String code;
    
    private Integer logStatus;
}
