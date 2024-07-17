package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目座位添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="SeatAddDto", description ="节目座位添加")
public class SeatAddDto {
    
    @Schema(name ="programId", type ="Long", description ="节目表id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long programId;
    
    @Schema(name ="ticketCategoryId", type ="Long", description ="节目票档id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long ticketCategoryId;
    
    @Schema(name ="rowCode", type ="Integer", description ="排号",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer rowCode;
    
    @Schema(name ="colCode", type ="Integer", description ="列号",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer colCode;
    
    @Schema(name ="seatType", type ="Integer", description ="座位类型 详见seatType枚举",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer seatType;
    
    @Schema(name ="price", type ="BigDecimal", description ="座位价格",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private BigDecimal price;
}
