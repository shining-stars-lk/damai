package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-11
 **/
@Data
@ApiModel(value="DelayOrderCancelDto", description ="延迟订单取消")
public class DelayOrderCancelDto {
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号")
    @NotNull
    private Long orderNumber;
}
