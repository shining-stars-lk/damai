package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 节目种类 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="ProgramCategoryVo", description ="节目种类")
public class ProgramCategoryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(name ="id", dataType ="Long", value ="区域id")
    private Long id;

    /**
     * 父id
     */
    @ApiModelProperty(name ="parentId", dataType ="Long", value ="父区域id")
    private Long parentId;

    /**
     * 名字
     */
    @ApiModelProperty(name ="name", dataType ="String", value ="区域名字")
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    @ApiModelProperty(name ="type", dataType ="Integer", value ="1:一级种类 2:二级种类")
    private Integer type;
}
