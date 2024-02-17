package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-12
 **/
@Data
@ApiModel(value="OrderCancelDto", description ="订单取消")
public class OrderCancelDto {
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号", required =true)
    @NotNull
    private Long orderNumber;
    
}
