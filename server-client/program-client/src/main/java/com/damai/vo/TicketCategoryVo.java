package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 节目票档表
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
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
