package com.example.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Data
public class ProductOrder {

    private String id;
    
    private String productId;
    
    private String productName;
    
    private BigDecimal productPrice;
    
    private Integer productAmount;
    
    private BigDecimal productTotalPrice;
    
    private String orderId;
    
    private Integer status;
}