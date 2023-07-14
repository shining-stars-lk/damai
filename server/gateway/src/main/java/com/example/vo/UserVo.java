package com.example.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-03
 **/
@Data
public class UserVo {
    
    private String id;
    
    private String name;
    
    private String password;
    
    private Integer age;
    
    private Integer status;
    
    private Date createTime;
    
    private String mobile;
    
    private Date editTime;
}
