package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人订单 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="OrderTicketUserCreateDto", description ="购票人订单创建")
public class OrderTicketUserCreateDto {
    
    @ApiModelProperty(name ="orderNumber", dataType ="Long", value ="订单编号", required =true)
    @NotNull
    private Long orderNumber;
    
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
    
    @ApiModelProperty(name ="seatInfo", dataType ="String", value ="座位信息", required =true)
    @NotBlank
    private String seatInfo;
    
    @ApiModelProperty(name ="orderPrice", dataType ="BigDecimal", value ="订单价格", required =true)
    @NotNull
    private BigDecimal orderPrice;
    
    @ApiModelProperty(name ="createOrderTime", dataType ="Date", value ="生成订单时间", required =true)
    @NotNull
    private Date createOrderTime;
    
}
