package com.example.dto;

import lombok.Data;

/**
 * @description:
 * @author: k
 * @create: 2022-08-31
 **/
@Data
public class CreateIndexDto {
    
    /**
     * 字段名
     * */
    private String paramName;
    
    /**
     * 字段类型
     * */
    private String paramType;
}
