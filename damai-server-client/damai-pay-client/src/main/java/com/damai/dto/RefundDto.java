package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 退款 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="RefundDto", description ="退款")
public class RefundDto {
    
    @Schema(name ="orderNumber", type ="Long", description ="订单号",requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String orderNumber;
    
    @Schema(name ="amount", type ="BigDecimal", description ="退款金额",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private BigDecimal amount;
    
    @Schema(name ="channel", type ="Integer", description ="退款渠道 alipay：支付宝 wx：微信",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private String channel;
    
    @Schema(name ="reason", type ="String", description ="退款原因")
    private String reason;
}
