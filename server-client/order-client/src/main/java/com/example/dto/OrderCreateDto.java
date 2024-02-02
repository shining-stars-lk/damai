package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-12
 **/
@Data
@ApiModel(value="OrderCreateDto", description ="订单创建")
public class OrderCreateDto {
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="订单编号", required =true)
    @NotNull
    private Long orderNumber;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id", required =true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id", required =true)
    @NotNull
    private Long userId;
    
    @ApiModelProperty(name ="programTitle", dataType ="String", value ="节目标题", required =true)
    @NotBlank
    private String programTitle;
    
    @ApiModelProperty(name ="programPlace", dataType ="String", value ="节目地点", required =true)
    @NotBlank
    private String programPlace;
    
    @ApiModelProperty(name ="programShowTime", dataType ="Date", value ="节目演出时间", required =true)
    @NotNull
    private Date programShowTime;
    
    @ApiModelProperty(name ="distributionMode", dataType ="String", value ="配送方式")
    private String distributionMode;
    
    @ApiModelProperty(name ="takeTicketMode", dataType ="String", value ="取票方式")
    private String takeTicketMode;
    
    @ApiModelProperty(name ="orderPrice", dataType ="BigDecimal", value ="订单价格", required =true)
    @NotNull
    private BigDecimal orderPrice;
    
    @ApiModelProperty(name ="createOrderTime", dataType ="Date", value ="生成订单时间", required =true)
    @NotNull
    private Date createOrderTime;
    
    @ApiModelProperty(name ="orderTicketUserCreateDtoList", dataType ="List<OrderTicketUserCreateDto>", value ="购票人订单集合", required =true)
    @NotNull
    private List<OrderTicketUserCreateDto> orderTicketUserCreateDtoList;
    
}
