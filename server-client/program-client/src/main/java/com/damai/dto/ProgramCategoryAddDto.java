package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@Data
@ApiModel(value="ProgramCategoryAddDto", description ="节目类型")
public class ProgramCategoryAddDto implements Serializable {

    private static final long serialVersionUID = 1L;
    

    /**
     * 父区域id
     */
    @ApiModelProperty(name ="parentId", dataType ="Long", value ="父区域id",required = true)
    @NotNull
    private Long parentId;

    /**
     * 区域名字
     */
    @ApiModelProperty(name ="name", dataType ="String", value ="区域名字",required = true)
    @NotNull
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    @ApiModelProperty(name ="type", dataType ="Integer", value ="1:一级种类 2:二级种类",required = true)
    @NotNull
    private Integer type;
}
