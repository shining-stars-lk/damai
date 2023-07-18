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
public class PsOrder {

    private String id;
    
    private String payOrderId;
    
    private BigDecimal payAmount;
    
    private Integer payChannelType;
    
    private Date payTime;
    
    private Integer status;
    
    private Date createTime;
    
    private String userId;
}
