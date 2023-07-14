package com.example.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-14
 **/
@Data
public class Product {
    
    private String id;
    
    private String name;
    
    private BigDecimal price;
    
    private Integer stock;
    
    private Integer status;
    
    private Date createTime;
}
