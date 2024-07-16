package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目座位添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="SeatBatchAddDto", description ="节目座位批量添加")
public class SeatBatchAddDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id",required = true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="ticketCategoryId", dataType ="SeatBatchRelateInfoAddDto", value ="节目座位相关信息",required = true)
    @NotNull
    private List<SeatBatchRelateInfoAddDto> seatBatchRelateInfoAddDtoList;
}
