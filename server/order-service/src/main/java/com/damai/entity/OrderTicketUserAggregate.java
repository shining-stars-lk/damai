package com.damai.entity;

import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-29
 **/
@Data
public class OrderTicketUserAggregate {
    
    private Long orderNumber;
    
    private Integer orderTicketUserCount;
}
