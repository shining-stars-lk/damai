package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 广播调用 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="BroadcastCallDto", description ="广播调用")
public class BroadcastCallDto {
    
    @ApiModelProperty(name ="serviceName", dataType ="String", value ="服务名", required =true)
    @NotBlank
    private String serviceName;
    
    @ApiModelProperty(name ="requestBody", dataType ="String", value ="请求体", required =true)
    @NotBlank
    private String requestBody;
}