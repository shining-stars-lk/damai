package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseData;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 座位 实体
 * @author: 阿宽不是程序员
 **/
@Data
@TableName("d_seat")
public class Seat extends BaseData implements Serializable {

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
     * 节目票档id
     * */
    private Long ticketCategoryId;
    
    /**
     * 排号
     */
    private Integer rowCode;
    
    /**
     * 列号
     */
    private Integer colCode;

    /**
     * 座位类型 详见seatType枚举
     */
    private Integer seatType;

    /**
     * 座位价格
     */
    private BigDecimal price;

    /**
     * 1未售卖 2锁定 3已售卖
     */
    private Integer sellStatus;
}
