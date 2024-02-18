package com.damai.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: job 实体
 * @author: 阿宽不是程序员
 **/
@Data
public class JobInfo {

    private Long id;
    
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
