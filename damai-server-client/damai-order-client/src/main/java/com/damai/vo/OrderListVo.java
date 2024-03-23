package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单列表 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="OrderListVo", description ="订单列表")
public class OrderListVo {
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号")
    private Long orderNumber;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id")
    private Long programId;

    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id")
    private Long userId;
    
    @ApiModelProperty(name ="programTitle", dataType ="String", value ="节目标题")
    private String programTitle;
    
    @ApiModelProperty(name ="programPlace", dataType ="String", value ="节目地点")
    private String programPlace;
    
    @ApiModelProperty(name ="programShowTime", dataType ="Date", value ="节目演出时间")
    private Date programShowTime;

    @ApiModelProperty(name ="orderPrice", dataType ="BigDecimal", value ="订单价格")
    private BigDecimal orderPrice;
    
    @ApiModelProperty(name ="payOrderType", dataType ="Integer", value ="支付订单方式")
    private Integer payOrderType;
    
    @ApiModelProperty(name ="orderStatus", dataType ="Integer", value ="订单状态 1:未支付 2:已取消 3:已支付 4:已退单")
    private Integer orderStatus;
    
    @ApiModelProperty(name ="createOrderTime", dataType ="Date", value ="生成订单时间")
    private Date createOrderTime;
    
    @ApiModelProperty(name ="ticketCount", dataType ="Integer", value ="票品张数")
    private Integer ticketCount;
}
