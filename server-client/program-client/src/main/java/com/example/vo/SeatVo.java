package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 节目票档表
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Data
@ApiModel(value="SeatVo", description ="座位")
public class SeatVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="座位id")
    private Long id;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id")
    private Long programId;
    
    @ApiModelProperty(name ="ticketCategoryId", dataType ="Long", value ="节目票档id")
    private Long ticketCategoryId;
    
    @ApiModelProperty(name ="rowCode", dataType ="Integer", value ="排号")
    private Integer rowCode;
  
    @ApiModelProperty(name ="colCode", dataType ="Integer", value ="列号")
    private Integer colCode;
    
    @ApiModelProperty(name ="seatType", dataType ="Integer", value ="座位类型")
    private Integer seatType;
    
    @ApiModelProperty(name ="seatTypeName", dataType ="String", value ="座位类型名")
    private String seatTypeName;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="座位价格")
    private BigDecimal price;
    
    @ApiModelProperty(name ="sellStatus", dataType ="Integer", value ="1未售卖 2锁定 3已售卖")
    private Integer sellStatus;
}
