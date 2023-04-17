package com.example.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/
@Setter
@Getter
public class GetOrderVo {
    
    private String id;
    
    private String name;
    
    private String productId;
    
    private String productName;
    
    private Integer productNumber; 
}
