package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-25
 **/
@Data
@ApiModel(value="PayOrderDto", description ="支付")
public class PayOrderDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    @NotNull
    private String id;
    
    @ApiModelProperty(name ="payChannelType", dataType ="Integer", value ="支付方式1.支付宝 2.微信", required =true)
    @NotNull
    private Integer payChannelType;
}
