package com.example.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: kuan
 * @create: 2023-09-15
 **/
@Data
public class GeoPointDto {
    /**
     * 字段名
     * */
    private String paramName;
    /**
     * 纬度值
     * */
    private BigDecimal latitude;
    /**
     * 经度值
     * */
    private BigDecimal longitude;
    
    
}
