package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 座位表
 * </p>
 *
 * @author k
 * @since 2024-01-11
 */
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
     * 排号
     */
    private String rowCode;

    /**
     * 列号
     */
    private String colCode;

    /**
     * 座位类型 详见seatType枚举
     */
    private Integer seatType;

    /**
     * 座位价格
     */
    private BigDecimal price;

    /**
     * 0:已买 1可卖
     */
    private Integer sellStatus;
}