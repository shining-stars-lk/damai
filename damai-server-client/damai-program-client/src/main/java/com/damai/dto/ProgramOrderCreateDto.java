package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目订单创建 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramOrderCreateDto", description ="节目订单创建")
public class ProgramOrderCreateDto {
    
    @Schema(name ="programId", type ="Long", description ="节目id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long programId;
    
    @Schema(name ="userId", type ="Long", description ="用户id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long userId;
    
    @Schema(name ="ticketUserIdList", type ="List<Long>", description ="购票人id集合",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private List<Long> ticketUserIdList;
    
    @Schema(name ="seatDtoList", type ="List<SeatDto>", description = "座位")
    private List<SeatDto> seatDtoList;
    
    @Schema(name ="ticketCategoryId", type ="Long", description = "节目票档id(如果不选座位，那么票档id必填)")
    private Long ticketCategoryId;
    
    @Schema(name ="ticketCount", type ="Integer", description = "购买票数量(如果不选座位，那么购买票数量必填)")
    private Integer ticketCount;
}
