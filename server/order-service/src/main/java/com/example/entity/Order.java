package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 节目类型表
 * </p>
 *
 * @author k
 * @since 2024-01-12
 */
@Data
@TableName("d_order")
public class Order extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 节目表id
     */
    private Long programId;

    /**
     * 用户id
     */
    private Long userId;
    
    /**
     * 节目标题
     * */
    private String programTitle;
    
    /**
     * 节目地点
     * */
    private String programPlace;
    
    /**
     * 节目演出时间
     * */
    private Date programShowTime;

    /**
     * 配送方式
     */
    private String distributionMode;

    /**
     * 取票方式
     */
    private String takeTicketMode;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 支付订单方式
     */
    private Integer payOrderType;

    /**
     * 订单状态 1:未支付 2:已取消 3:已支付 4:已退单
     */
    private Integer orderStatus;

    /**
     * 生成订单时间
     */
    private Date createOrderTime;

    /**
     * 取消订单时间
     */
    private Date cancelOrderTime;

    /**
     * 支付订单时间
     */
    private Date payOrderTime;
}
