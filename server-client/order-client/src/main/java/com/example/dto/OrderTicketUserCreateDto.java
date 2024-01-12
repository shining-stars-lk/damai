package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-12
 **/
@Data
@ApiModel(value="OrderTicketUserCreateDto", description ="购票人订单创建")
public class OrderTicketUserCreateDto {
    
    @ApiModelProperty(name ="orderId", dataType ="Long", value ="订单id", required =true)
    @NotNull
    private Long orderId;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id", required =true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id", required =true)
    @NotNull
    private Long userId;
    
    @ApiModelProperty(name ="ticketUserId", dataType ="Long", value ="购票人id", required =true)
    @NotNull
    private Long ticketUserId;
    
    @ApiModelProperty(name ="seatId", dataType ="Long", value ="座位id", required =true)
    @NotNull
    private Long seatId;
    
    @ApiModelProperty(name ="orderPrice", dataType ="BigDecimal", value ="订单价格", required =true)
    @NotNull
    private BigDecimal orderPrice;
    
    @ApiModelProperty(name ="createOrderTime", dataType ="Date", value ="生成订单时间", required =true)
    @NotNull
    private Date createOrderTime;
    
}
