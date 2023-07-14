package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-04-17
 **/
@Data
@ApiModel(value="GetOrderVo", description ="订单")
public class GetOrderVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="订单id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="订单名字", required =true)
    private String name;
    
    @ApiModelProperty(name ="productId", dataType ="String", value ="产品id", required =true)
    private String productId;
    
    @ApiModelProperty(name ="productName", dataType ="String", value ="产品名字", required =true)
    private String productName;
    
    @ApiModelProperty(name ="productPrice", dataType ="BigDecimal", value ="产品单价", required =true)
    private BigDecimal productPrice;
    
    @ApiModelProperty(name ="productNumber", dataType ="Integer", value ="产品数量", required =true)
    private Integer productNumber;
}
