package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 退款 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="RefundDto", description ="退款")
public class RefundDto {
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单号",required = true)
    @NotBlank
    private String orderNumber;
    
    @ApiModelProperty(name ="amount", dataType ="BigDecimal", value ="退款金额",required = true)
    @NotNull
    private BigDecimal amount;
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="退款渠道 alipay：支付宝 wx：微信",required = true)
    @NotNull
    private String channel;
    
    @ApiModelProperty(name ="reason", dataType ="String", value ="退款原因")
    private String reason;
}
