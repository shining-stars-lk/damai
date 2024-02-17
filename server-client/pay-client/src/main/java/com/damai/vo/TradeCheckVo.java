package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@Data
@ApiModel(value="TradeCheckVo", description ="交易状态结果")
public class TradeCheckVo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="success", dataType ="boolean", value ="是否成功")
    private boolean success;
    
    @ApiModelProperty(name ="payBillStatus", dataType ="Integer", value ="支付账单状态")
    private Integer payBillStatus;
    
    @ApiModelProperty(name ="outTradeNo", dataType ="String", value ="商户订单号")
    private String outTradeNo;
    
    @ApiModelProperty(name ="totalAmount", dataType ="BigDecimal", value ="支付金额")
    private BigDecimal totalAmount;
}
