package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目票档详情 Vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="TicketCategoryDetailVo", description ="节目票档详情")
public class TicketCategoryDetailVo {
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="节目表id",required = true)
    private Long programId;
    
    @ApiModelProperty(name ="introduce", dataType ="String", value ="介绍",required = true)
    private String introduce;
    
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="价格",required = true)
    private BigDecimal price;
    
    @ApiModelProperty(name ="totalNumber", dataType ="Long", value ="总数量",required = true)
    private Long totalNumber;
    
    @ApiModelProperty(name ="remainNumber", dataType ="Long", value ="剩余数量",required = true)
    private Long remainNumber;
    
    
}
