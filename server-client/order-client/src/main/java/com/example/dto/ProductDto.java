package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
@Data
@ApiModel(value="ProductDto", description ="产品")
public class ProductDto {
    
    @ApiModelProperty(name ="productId", dataType ="String", value ="产品id", required =true)
    @NotNull
    private String productId;
    
    @ApiModelProperty(name ="productName", dataType ="String", value ="产品名字", required =true)
    @NotNull
    private String productName;
    
    @ApiModelProperty(name ="productPrice", dataType ="BigDecimal", value ="产品单价", required =true)
    @NotNull
    private BigDecimal productPrice;
    
    @ApiModelProperty(name ="productAmount", dataType ="Integer", value ="产品数量", required =true)
    @NotNull
    private Integer productAmount;
}
