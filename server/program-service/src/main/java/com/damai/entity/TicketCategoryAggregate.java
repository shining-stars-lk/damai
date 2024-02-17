package com.damai.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 节目票档表(聚合查询使用)
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
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
