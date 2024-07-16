package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单支付 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="OrderPayDto", description ="订单支付")
public class OrderPayDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="platform", dataType ="Integer", value ="支付平台 1：小程序  2：H5  3：pc网页  4：app", required = true)
    @NotNull
    private Integer platform;
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号", required = true)
    @NotNull
    private Long orderNumber;
    
    @ApiModelProperty(name ="subject", dataType ="String", value ="订单标题", required = true)
    @NotBlank
    private String subject;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="价格", required = true)
    @NotNull
    private BigDecimal price;
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="支付渠道 alipay：支付宝 wx：微信", required = true)
    @NotBlank
    private String channel;

    @ApiModelProperty(name ="payBillType", dataType ="Integer", value ="支付种类 1节目",required = true)
    @NotNull
    private Integer payBillType;
}
