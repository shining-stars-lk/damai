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
public class JobRunRecord {

    private String id;
    
    private String jobId;
    
    private String runInfo;
    
    private Integer status;
    
    private Date createTime;
    
    private Integer runStatus;
    
    private String traceId;
}
