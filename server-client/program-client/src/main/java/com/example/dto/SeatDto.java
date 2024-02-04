package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
@ApiModel(value="SeatDto", description ="座位")
public class SeatDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="座位id",required = true)
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="ticketCategoryId", dataType ="Long", value ="节目票档id",required = true)
    @NotNull
    private Long ticketCategoryId;
    
    @ApiModelProperty(name ="rowCode", dataType ="String", value ="排号",required = true)
    @NotNull
    private Integer rowCode;
    
    @ApiModelProperty(name ="colCode", dataType ="String", value ="列号",required = true)
    @NotNull
    private Integer colCode;
    
    @ApiModelProperty(name ="seatType", dataType ="Integer", value ="座位类型")
    private Integer seatType;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="座位价格",required = true)
    @NotBlank
    private BigDecimal price;
}
