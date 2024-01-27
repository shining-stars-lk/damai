package com.example.dto;

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
@ApiModel(value="OrderGetDto", description ="订单查看")
public class OrderGetDto {
    
    @ApiModelProperty(name ="orderId", dataType ="Long", value ="订单id", required =true)
    @NotNull
    private Long orderId;
    
}
