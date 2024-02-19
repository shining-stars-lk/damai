package com.damai.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: job记录 实体
 * @author: 阿宽不是程序员
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
