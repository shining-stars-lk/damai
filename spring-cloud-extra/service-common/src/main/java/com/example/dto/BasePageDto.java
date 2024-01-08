package com.example.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-08
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
