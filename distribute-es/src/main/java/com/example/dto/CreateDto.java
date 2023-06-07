package com.example.dto;

import lombok.Data;

/**
 * @description:
 * @author: k
 * @create: 2022-08-31
 **/
@Data
public class CreateDto {
    
    /**
     * 字段名
     * */
    private String paramName;
    /**
     * 字段值
     * */
    private Object paramValue;
}
