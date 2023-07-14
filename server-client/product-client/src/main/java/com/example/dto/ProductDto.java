package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-14
 **/
@Data
@ApiModel(value="ProductDto", description ="产品")
public class ProductDto {
    
    @ApiModelProperty(name ="name", dataType ="String", value ="产品名字", required =true)
    @NotBlank
    private String name;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="价格", required =true)
    @NotNull
    private BigDecimal price;
    
    @ApiModelProperty(name ="stock", dataType ="Integer", value ="库存数量", required =true)
    @NotNull
    private Integer stock;

    @ApiModelProperty(name ="saveRedis", dataType ="Integer", value ="1 存入redis", required =true)
    private Integer saveRedis;
}
