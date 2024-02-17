package com.example.dto;

import lombok.Data;

/**
 * @description:
 * @author: 星哥
 * @create: 2023-08-31
 **/
@Data
public class EsDocumentMappingDto {
    
    /**
     * 字段名
     * */
    private String paramName;
    
    /**
     * 字段类型
     * */
    private String paramType;
}