package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单支付后状态检查 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="OrderPayCheckVo", description ="订单支付后状态检查")
public class OrderPayCheckVo {
    
    @Schema(name ="id", type ="Long", description ="订单id")
    private Long id;
    
    @Schema(name ="programId", type ="Long", description ="节目表id")
    private Long programId;

    @Schema(name ="userId", type ="Long", description ="用户id")
    private Long userId;

    @Schema(name ="orderPrice", type ="BigDecimal", description ="订单价格")
    private BigDecimal orderPrice;
    
    @Schema(name ="payOrderType", type ="Integer", description ="支付订单方式")
    private Integer payOrderType;
    
    @Schema(name ="orderStatus", type ="Integer", description ="订单状态 1:未支付 2:已取消 3:已支付 4:已退单")
    private Integer orderStatus;
    
    @Schema(name ="createOrderTime", type ="Date", description ="生成订单时间")
    private Date createOrderTime;
    
    @Schema(name ="cancelOrderTime", type ="Date", description ="取消订单时间")
    private Date cancelOrderTime;
    
    @Schema(name ="payOrderTime", type ="Date", description ="支付订单时间")
    private Date payOrderTime;
}
