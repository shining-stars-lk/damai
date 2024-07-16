package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目票档 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="TicketCategoryDto", description ="节目票档")
public class TicketCategoryDto {
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="id",required = true)
    @NotNull
    private Long id;
    
}
