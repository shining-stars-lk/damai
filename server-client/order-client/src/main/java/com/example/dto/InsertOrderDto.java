package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Data
@ApiModel(value="InsertOrderDto", description ="订单添加")
public class InsertOrderDto {
    
    @ApiModelProperty(name ="productDtoList", dataType ="ProductDto[]", value ="产品列表", required =true)
    @NotNull
    private List<ProductDto> productDtoList;
    
    @ApiModelProperty(name ="payAmount", dataType ="BigDecimal", value ="支付价格", required =true)
    @NotNull
    private BigDecimal payAmount;
    
    @ApiModelProperty(name ="payChannelType", dataType ="Integer", value ="支付方式1.支付宝 2.微信", required =true)
    @NotNull
    private Integer payChannelType;
    
    @ApiModelProperty(name ="userId", dataType ="String", value ="userId", required =true)
    private String userId;
}
