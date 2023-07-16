package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-30
 **/
@Data
@ApiModel(value="RuleDto", description ="普通规则")
public class RuleDto {
    
    @ApiModelProperty(name ="statTime", dataType ="Integer", value ="统计时间", required =true)
    @NotNull
    private Integer statTime;
    
    @ApiModelProperty(name ="statTimeType", dataType ="Integer", value ="统计时间类型 1:秒 2:分钟", required =true)
    @NotNull
    private Integer statTimeType;
   
    @ApiModelProperty(name ="threshold", dataType ="Integer", value ="阈值", required =true)
    @NotNull
    private Integer threshold;
    
    @ApiModelProperty(name ="effectiveTime", dataType ="Integer", value ="规则生效限制时间", required =true)
    @NotNull
    private Integer effectiveTime;
    
    @ApiModelProperty(name ="effectiveTimeType", dataType ="Integer", value ="规则生效限制时间类型 1:秒 2:分钟", required =true)
    @NotNull
    private Integer effectiveTimeType;
    
    private String limitApi;
    
    @ApiModelProperty(name ="message", dataType ="String", value ="提示信息")
    private String message;
}
