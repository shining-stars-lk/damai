package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目座位添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="SeatBatchAddDto", description ="节目座位批量添加")
public class SeatBatchAddDto {
    
    @Schema(name ="programId", type ="Long", description ="节目表id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long programId;
    
    @Schema(name ="ticketCategoryId", type ="SeatBatchRelateInfoAddDto", description ="节目座位相关信息",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private List<SeatBatchRelateInfoAddDto> seatBatchRelateInfoAddDtoList;
}
