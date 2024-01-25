package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@Data
@ApiModel(value="TradeCheckDto", description ="交易状态入参")
public class TradeCheckDto {
    
    @ApiModelProperty(name ="outTradeNo", dataType ="String", value ="商户订单号", required = true)
    @NotBlank
    private String outTradeNo;
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="支付渠道 alipay：支付宝 wx：微信")
    @NotNull
    private String channel;
}
