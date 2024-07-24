package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人订单 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="OrderTicketUserCreateDto", description ="购票人订单创建")
public class OrderTicketUserCreateDto {
    
    @Schema(name ="orderNumber", type ="Long", description ="订单编号", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long orderNumber;
    
    @Schema(name ="programId", type ="Long", description ="节目表id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long programId;
    
    @Schema(name ="userId", type ="Long", description ="用户id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long userId;
    
    @Schema(name ="ticketUserId", type ="Long", description ="购票人id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long ticketUserId;
    
    @Schema(name ="seatId", type ="Long", description ="座位id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long seatId;
    
    @Schema(name ="seatInfo", type ="String", description ="座位信息", requiredMode= RequiredMode.REQUIRED)
    @NotBlank
    private String seatInfo;
    
    @Schema(name ="ticketCategoryId", type ="Long", description ="节目票档id", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long ticketCategoryId;
    
    @Schema(name ="orderPrice", type ="BigDecimal", description ="订单价格", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private BigDecimal orderPrice;
    
    @Schema(name ="createOrderTime", type ="Date", description ="生成订单时间", requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Date createOrderTime;
    
}
