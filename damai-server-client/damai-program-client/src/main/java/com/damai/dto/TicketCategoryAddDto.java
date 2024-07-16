package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目票档添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="TicketCategoryAddDto", description ="节目票档添加")
public class TicketCategoryAddDto {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id",required = true)
    @NotNull
    private Long programId;
    
    @ApiModelProperty(name ="introduce", dataType ="String", value ="介绍",required = true)
    @NotBlank
    private String introduce;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="价格",required = true)
    @NotNull
    private BigDecimal price;
    
    @ApiModelProperty(name ="totalNumber", dataType ="Long", value ="总数量",required = true)
    @NotNull
    private Long totalNumber;
    
    @ApiModelProperty(name ="remainNumber", dataType ="Long", value ="剩余数量",required = true)
    @NotNull
    private Long remainNumber;
    
    
}
