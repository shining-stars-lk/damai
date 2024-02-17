package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 购票人表
 * </p>
 *
 * @author k
 * @since 2024-01-09
 */
@Data
@ApiModel(value="OrderPayDto", description ="订单支付")
public class OrderPayDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="platform", dataType ="Integer", value ="支付平台 1：小程序  2：H5  3：pc网页  4：app")
    @NotNull
    private Integer platform;
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号")
    @NotNull
    private Long orderNumber;
    
    @ApiModelProperty(name ="subject", dataType ="String", value ="订单标题")
    @NotBlank
    private String subject;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="价格")
    @NotNull
    private BigDecimal price;
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="支付渠道 alipay：支付宝 wx：微信")
    @NotBlank
    private String channel;

    @ApiModelProperty(name ="payBillType", dataType ="Integer", value ="支付种类 1节目")
    @NotNull
    private Integer payBillType;
}
