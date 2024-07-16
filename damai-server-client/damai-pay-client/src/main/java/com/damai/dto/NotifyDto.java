package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付回调通知 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="NotifyDto", description ="支付回调通知")
public class NotifyDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="支付渠道 alipay：支付宝 wx：微信",required = true)
    @NotNull
    private String channel;

    @ApiModelProperty(name ="params", dataType ="Map<String, String>", value ="回调参数",required = true)
    @NotNull
    private Map<String, String> params;
}
