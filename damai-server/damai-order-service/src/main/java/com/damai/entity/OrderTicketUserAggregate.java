package com.damai.entity;

import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人订单聚合统计 实体
 * @author: 阿星不是程序员
 **/
@Data
public class OrderTicketUserAggregate {
    
    private Long orderNumber;
    
    private Integer orderTicketUserCount;
}
