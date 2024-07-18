package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目种类 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramCategoryVo", description ="节目种类")
public class ProgramCategoryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @Schema(name ="id", type ="Long", description ="区域id")
    private Long id;

    /**
     * 父id
     */
    @Schema(name ="parentId", type ="Long", description ="父区域id")
    private Long parentId;

    /**
     * 名字
     */
    @Schema(name ="name", type ="String", description ="区域名字")
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    @Schema(name ="type", type ="Integer", description ="1:一级种类 2:二级种类")
    private Integer type;
}
