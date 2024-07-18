package com.damai.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目类型添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="ProgramCategoryAddDto", description ="节目类型")
public class ProgramCategoryAddDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    

    /**
     * 父区域id
     */
    @Schema(name ="parentId", type ="Long", description ="父区域id",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Long parentId;

    /**
     * 区域名字
     */
    @Schema(name ="name", type ="String", description ="区域名字",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    @Schema(name ="type", type ="Integer", description ="1:一级种类 2:二级种类",requiredMode= RequiredMode.REQUIRED)
    @NotNull
    private Integer type;
}
