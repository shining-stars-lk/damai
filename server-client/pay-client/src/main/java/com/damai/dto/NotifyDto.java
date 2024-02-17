package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 购票人表
 * </p>
 *
 * @author k
 * @since 2024-01-09
 */
@Data
@ApiModel(value="NotifyDto", description ="支付回调通知")
public class NotifyDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="channel", dataType ="Integer", value ="支付渠道 alipay：支付宝 wx：微信")
    @NotNull
    private String channel;

    @ApiModelProperty(name ="params", dataType ="Map<String, String>", value ="回调参数")
    @NotNull
    private Map<String, String> params;
}
