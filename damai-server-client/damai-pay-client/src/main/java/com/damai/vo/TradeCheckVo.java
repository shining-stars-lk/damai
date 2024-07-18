package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 交易状态查询 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="TradeCheckVo", description ="交易状态结果")
public class TradeCheckVo implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Schema(name ="success", type ="boolean", description ="是否成功")
    private boolean success;
    
    @Schema(name ="payBillStatus", type ="Integer", description ="支付账单状态")
    private Integer payBillStatus;
    
    @Schema(name ="outTradeNo", type ="String", description ="商户订单号")
    private String outTradeNo;
    
    @Schema(name ="totalAmount", type ="BigDecimal", description ="支付金额")
    private BigDecimal totalAmount;
}
