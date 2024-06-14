package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票订单信息 vo
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="OrderTicketInfoVo", description ="购票订单信息")
public class OrderTicketInfoVo {
    
    @ApiModelProperty(name ="seatInfo", dataType ="String", value ="座位信息")
    private String seatInfo;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="单价")
    private BigDecimal price;
    
    @ApiModelProperty(name ="quantity", dataType ="Integer", value ="数量")
    private Integer quantity;
    
    @ApiModelProperty(name ="favourablePrice", dataType ="BigDecimal", value ="优惠")
    private BigDecimal favourablePrice;
    
    @ApiModelProperty(name ="relPrice", dataType ="BigDecimal", value ="小计")
    private BigDecimal relPrice;
    
}
