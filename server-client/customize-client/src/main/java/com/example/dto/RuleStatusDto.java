package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="RuleStatusDto", description ="普通规则状态修改")
public class RuleStatusDto {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="普通规则id", required =true)
    @NotBlank
    private String id;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="状态 1生效 0禁用", required =true)
    @NotNull
    private Integer status;
}
