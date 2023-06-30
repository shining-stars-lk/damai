package com.example.entity;

import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Data
public class Rule {
    
    private String id;

    private Integer statisticTime;
    
    private Integer statisticTimeType;
    
    private Integer thresholdValue;
    
    private Integer limitTime;
    
    private Integer limitTimeType;
    
    private Integer message;
    
    private Integer status;
    
    private Date createTime;
}
