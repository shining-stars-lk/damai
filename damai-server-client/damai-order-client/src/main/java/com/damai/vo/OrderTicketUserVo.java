package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人订单信息 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="OrderTicketUserVo", description ="购票人订单信息")
public class OrderTicketUserVo {

    /**
     * 主键id
     */
    @ApiModelProperty(name ="id", dataType ="Long", value ="购票人订单id")
    private Long id;
    
    @ApiModelProperty(name ="orderId", dataType ="Long", value ="订单id")
    private Long orderId;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id")
    private Long programId;

    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id")
    private Long userId;
    
    @ApiModelProperty(name ="ticketUserId", dataType ="Long", value ="购票人id")
    private Long ticketUserId;
    
    @ApiModelProperty(name ="seatId", dataType ="Long", value ="座位id")
    private Long seatId;
    
    @ApiModelProperty(name ="seatInfo", dataType ="String", value ="座位信息")
    private String seatInfo;
    
    @ApiModelProperty(name ="orderPrice", dataType ="BigDecimal", value ="订单价格")
    private BigDecimal orderPrice;
    
    @ApiModelProperty(name ="payOrderPrice", dataType ="BigDecimal", value ="支付订单价格")
    private BigDecimal payOrderPrice;
    
    @ApiModelProperty(name ="payOrderType", dataType ="Integer", value ="支付订单方式")
    private Integer payOrderType;
    
    @ApiModelProperty(name ="orderStatus", dataType ="Integer", value ="订单状态 1:未支付 2:已取消 3:已支付 4:已退单")
    private Integer orderStatus;
    
    @ApiModelProperty(name ="createOrderTime", dataType ="Date", value ="生成订单时间")
    private Date createOrderTime;
    
    @ApiModelProperty(name ="cancelOrderTime", dataType ="Date", value ="取消订单时间")
    private Date cancelOrderTime;
    
    @ApiModelProperty(name ="payOrderTime", dataType ="Date", value ="支付订单时间")
    private Date payOrderTime;
}
