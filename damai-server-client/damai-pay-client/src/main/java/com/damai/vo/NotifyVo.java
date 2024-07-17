package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 支付回调结果 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="NotifyVo", description ="交易状态结果")
public class NotifyVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="outTradeNo", type ="String", description ="商户订单号")
    private String outTradeNo;
    
    @Schema(name ="payResult", type ="String", description ="回调返回结果")
    private String payResult;
}
