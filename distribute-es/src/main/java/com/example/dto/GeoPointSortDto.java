package com.example.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description:
 * @author: 星哥
 * @create: 2023-09-15
 **/
@Data
public class GeoPointSortDto {
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
