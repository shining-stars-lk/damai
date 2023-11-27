package com.example.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-14
 **/
@Data
public class Product {
    
    private Long id;
    
    private String name;
    
    private BigDecimal price;
    
    private Integer stock;
    
    private Integer status;
    
    private Date createTime;
}
