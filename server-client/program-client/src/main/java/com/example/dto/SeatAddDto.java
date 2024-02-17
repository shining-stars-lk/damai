package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 座位表
 * </p>
 *
 * @author k
 * @since 2024-01-11
 */
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