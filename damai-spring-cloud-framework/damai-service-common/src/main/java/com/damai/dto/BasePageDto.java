package com.damai.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 分页dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="BasePageDto", description ="分页")
public class BasePageDto {
    
    @ApiModelProperty(name ="pageNumber", dataType ="Integer", value ="页码",required = true)
    @NotNull
    private Integer pageNumber;
    
    @ApiModelProperty(name ="pageSize", dataType ="Integer", value ="页大小",required = true)
    @NotNull
    private Integer pageSize;
}
