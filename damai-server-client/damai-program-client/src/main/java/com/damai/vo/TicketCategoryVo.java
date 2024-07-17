package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 票档 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="TicketCategoryVo", description ="票档")
public class TicketCategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Schema(name ="id", type ="Long", description ="主键id")
    private Long id;

    /**
     * 介绍
     */
    @Schema(name ="introduce", type ="String", description ="介绍")
    private String introduce;

    /**
     * 价格
     */
    @Schema(name ="price", type ="BigDecimal", description ="价格")
    private BigDecimal price;
}
