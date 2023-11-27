package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-05
 **/

@Data
public class Department {
    
    private Long id;
    
    private String name;
    
    private String typeCode;
    
    private Date createTime;
    
    private Integer status;
}
