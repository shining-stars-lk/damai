
package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目座位相关信息 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="SeatBatchRelateInfoAddDto", description ="节目座位相关信息批量添加")
public class SeatBatchRelateInfoAddDto {
    
    @ApiModelProperty(name ="ticketCategoryId", dataType ="Long", value ="节目票档id",required = true)
    @NotNull
    private Long ticketCategoryId;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="座位价格",required = true)
    @NotNull
    private BigDecimal price;
    
    @ApiModelProperty(name ="count", dataType ="Integer", value ="添加的座位数量",required = true)
    @NotNull
    private Integer count;
}
