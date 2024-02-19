package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 地区 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="AreaVo", description ="区域数据")
public class AreaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域id
     */
    @ApiModelProperty(name ="id", dataType ="Long", value ="区域id")
    private Long id;

    /**
     * 父区域id
     */
    @ApiModelProperty(name ="parentId", dataType ="Long", value ="父区域id")
    private Long parentId;
    /**
     * 区域名字
     */
    @ApiModelProperty(name ="name", dataType ="Long", value ="区域名字")
    private String name;

    /**
     * 1:省 2:区 3:县
     */
    @ApiModelProperty(name ="type", dataType ="Integer", value ="1:省 2:区 3:县")
    private Integer type;

    /**
     * 1:是 0:否
     */
    @ApiModelProperty(name ="municipality", dataType ="Boolean", value ="1:是 0:否")
    private Integer municipality;
}
