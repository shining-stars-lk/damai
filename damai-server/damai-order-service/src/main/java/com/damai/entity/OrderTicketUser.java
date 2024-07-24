package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseTableData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人订单 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_order_ticket_user")
public class OrderTicketUser extends BaseTableData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 订单编号
     * */
    private Long orderNumber;

    /**
     * 节目表id
     */
    private Long programId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 购票人id
     */
    private Long ticketUserId;

    /**
     * 座位id
     */
    private Long seatId;
    
    /**
     * 座位信息
     * */
    private String seatInfo;
    
    /**
     * 节目票档id
     * */
    private Long ticketCategoryId;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 支付订单价格
     */
    private BigDecimal payOrderPrice;

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
