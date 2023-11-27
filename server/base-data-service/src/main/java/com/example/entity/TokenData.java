package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-05
 **/
@Data
public class TokenData {

    private Long id;
    
    private String name;
    
    private String secret;
    
    private Integer status;
    
    private Date createTime;
}
