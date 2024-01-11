package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 节目票档表
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Data
@TableName("d_ticket_category")
public class TicketCategory extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 节目表id
     */
    private Long programId;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 总数量
     * */
    private Long totalNumber;
    
    /**
     * 剩余数量
     * */
    private Long remainNumber;
    
    
}
