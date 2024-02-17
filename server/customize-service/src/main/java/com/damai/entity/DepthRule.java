package com.damai.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Data
public class DepthRule {
    
    private Long id;
    
    private String startTimeWindow;
    
    private String endTimeWindow;

    private Integer statTime;
    
    private Integer statTimeType;
    
    private Integer threshold;
    
    private Integer effectiveTime;
    
    private Integer effectiveTimeType;
    
    private String limitApi;
    
    private String message;
    
    private Integer status;
    
    private Date createTime;
}
