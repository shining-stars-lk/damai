package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目座位添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="SeatAddDto", description ="节目座位添加")
public class SeatAddDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id",required = true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="ticketCategoryId", dataType ="Long", value ="节目票档id",required = true)
    @NotNull
    private Long ticketCategoryId;
    
    @ApiModelProperty(name ="rowCode", dataType ="Integer", value ="排号",required = true)
    @NotNull
    private Integer rowCode;
    
    @ApiModelProperty(name ="colCode", dataType ="Integer", value ="列号",required = true)
    @NotNull
    private Integer colCode;
    
    @ApiModelProperty(name ="seatType", dataType ="Integer", value ="座位类型 详见seatType枚举",required = true)
    @NotNull
    private Integer seatType;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="座位价格",required = true)
    @NotNull
    private BigDecimal price;
}
