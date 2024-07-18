package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseTableData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 票档 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_ticket_category")
public class TicketCategory extends BaseTableData implements Serializable {

    @Serial
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
