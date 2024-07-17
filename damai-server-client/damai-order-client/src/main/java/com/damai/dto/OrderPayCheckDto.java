package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 订单支付后状态检查 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="OrderPayCheckDto", description ="订单支付后状态检查")
public class OrderPayCheckDto {
    
    @Schema(name ="orderNumber", type ="String", description ="订单编号", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long orderNumber;
    
    @Schema(name ="payChannelType", type ="Integer", description ="支付方式1.支付宝 2.微信", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer payChannelType;
}
