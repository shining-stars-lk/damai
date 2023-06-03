package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-03
 **/

@Data
public class User {
    
    private String id;
    
    private String name;
    
    private String password;
    
    private Integer age;
    
    private Integer status;
    
    private Date createTime;
}
