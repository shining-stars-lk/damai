package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="DepthRuleStatusDto", description ="深度规则状态修改")
public class DepthRuleStatusDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="深度规则id", required =true)
    private Long id;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="状态 1生效 0禁用", required =true)
    @NotNull
    private Integer status;
}
