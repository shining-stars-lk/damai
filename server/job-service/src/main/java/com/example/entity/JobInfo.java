package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-28
 **/
@Data
public class JobInfo {

    private String id;
    
    private String name;
    
    private String description;
    
    private String url;
    
    private String headers;
    
    private Integer method;
    
    private String params;
    
    private Integer status;
    
    private Date createTime;
    
    private Integer retry;
    
    private Integer retryNumber;
}
