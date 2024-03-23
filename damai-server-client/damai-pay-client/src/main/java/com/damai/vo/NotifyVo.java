package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付回调结果 vo
 * @author: 阿宽不是程序员
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
