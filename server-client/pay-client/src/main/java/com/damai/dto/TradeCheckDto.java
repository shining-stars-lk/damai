package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 交易状态 dto
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="TradeCheckDto", description ="交易状态入参")
public class TradeCheckDto {
    
    @ApiModelProperty(name ="outTradeNo", dataType ="String", value ="商户订单号", required = true)
    @NotBlank
    private String outTradeNo;
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="支付渠道 alipay：支付宝 wx：微信")
    @NotBlank
    private String channel;
}
