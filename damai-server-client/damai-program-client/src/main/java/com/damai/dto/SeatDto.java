package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 座位 dto
 * @author: 阿星不是程序员
 **/
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
    @NotNull
    private BigDecimal price;
}
