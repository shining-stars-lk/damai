package com.example.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-14
 **/
@Data
public class ProductOrder {

    private Long id;
    
    private Long productId;
    
    private String productName;
    
    private BigDecimal productPrice;
    
    private Integer productAmount;
    
    private BigDecimal productTotalPrice;
    
    private Long orderId;
    
    private Integer status;
}
