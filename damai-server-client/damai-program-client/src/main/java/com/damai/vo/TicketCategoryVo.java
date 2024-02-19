package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 票档 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="TicketCategoryVo", description ="票档")
public class TicketCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(name ="id", dataType ="Long", value ="主键id")
    private Long id;

    /**
     * 介绍
     */
    @ApiModelProperty(name ="introduce", dataType ="String", value ="介绍")
    private String introduce;

    /**
     * 价格
     */
    @ApiModelProperty(name ="price", dataType ="BigDecimal", value ="价格")
    private BigDecimal price;
}
