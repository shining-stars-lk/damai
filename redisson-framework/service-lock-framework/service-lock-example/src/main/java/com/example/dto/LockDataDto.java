package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-17
 **/
@Data
@ApiModel(value="LockDataDto", description ="分布式锁测试数据")
public class LockDataDto {
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="id", required =true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="stock", dataType ="Integer", value ="库存", required =true)
    @NotNull
    private Integer stock;
}
