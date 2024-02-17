package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-25
 **/
@Data
@ApiModel(value="NotifyVo", description ="交易状态结果")
public class NotifyVo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="outTradeNo", dataType ="String", value ="商户订单号")
    private String outTradeNo;
    
    @ApiModelProperty(name ="payResult", dataType ="String", value ="回调返回结果")
    private String payResult;
}
