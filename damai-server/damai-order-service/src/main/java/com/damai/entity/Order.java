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
 * @description: 订单 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_order")
public class Order extends BaseTableData implements Serializable {

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
     * 节目图片介绍
     * */
    private String programItemPicture;

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
     * 节目是否允许选座 1:允许选座 0:不允许选座
     * */
    private Integer programPermitChooseSeat;

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
