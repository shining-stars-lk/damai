package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
