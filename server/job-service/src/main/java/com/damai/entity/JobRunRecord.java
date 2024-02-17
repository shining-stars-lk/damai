package com.damai.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Data
public class JobRunRecord {

    private Long id;
    
    private Long jobId;
    
    private String runInfo;
    
    private Integer status;
    
    private Date createTime;
    
    private Integer runStatus;
    
    private String traceId;
}
