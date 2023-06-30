package com.example.vo;

import lombok.Data;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-30
 **/
@Data
public class RuleVo {
    
    private String id;

    private Integer statisticTime;

    private Integer statisticTimeType;

    private Integer thresholdValue;

    private Integer limitTime;

    private Integer limitTimeType;

    private Integer message;
    
    private Integer status;
}
