package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-25
 **/
@Data
@ApiModel(value="OrderPayCheckDto", description ="订单支付后状态检查")
public class OrderPayCheckDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="订单id", required =true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="payChannelType", dataType ="Integer", value ="支付方式1.支付宝 2.微信", required =true)
    @NotNull
    private Integer payChannelType;
}
