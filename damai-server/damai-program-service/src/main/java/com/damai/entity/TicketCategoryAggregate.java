package com.damai.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 票档统计 实体
 * @author: 阿星不是程序员
 **/
@Data
public class TicketCategoryAggregate implements Serializable {
    
    /**
     * 节目表id
     */
    private Long programId;
    
    /**
     * 最低价格
     */
    private BigDecimal minPrice;
    
    /**
     * 最高价格
     */
    private BigDecimal maxPrice;
}
