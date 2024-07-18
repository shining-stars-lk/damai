package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票订单信息 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="OrderTicketInfoVo", description ="购票订单信息")
public class OrderTicketInfoVo {
    
    @Schema(name ="seatInfo", type ="String", description ="座位信息")
    private String seatInfo;
    
    @Schema(name ="price", type ="BigDecimal", description ="单价")
    private BigDecimal price;
    
    @Schema(name ="quantity", type ="Integer", description ="数量")
    private Integer quantity;
    
    @Schema(name ="favourablePrice", type ="BigDecimal", description ="优惠")
    private BigDecimal favourablePrice;
    
    @Schema(name ="relPrice", type ="BigDecimal", description ="小计")
    private BigDecimal relPrice;
    
}
