package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 渠道数据查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="GetChannelDataByCodeDto", description ="渠道数据查询")
public class GetChannelDataByCodeDto {
    
    @ApiModelProperty(name ="code", dataType ="String", value ="code码", required =true)
    @NotBlank
    private String code;
    
}