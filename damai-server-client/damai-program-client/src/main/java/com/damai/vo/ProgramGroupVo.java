package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目分组 vo
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramGroupVo", description ="节目分组")
public class ProgramGroupVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="主键id")
    private Long id;
    
    @ApiModelProperty(name ="programSimpleInfoVoList", dataType ="List<ProgramSimpleInfoVo>", value ="节目简单信息集合")
    private List<ProgramSimpleInfoVo> programSimpleInfoVoList;
    
    @ApiModelProperty(name ="recentShowTime", dataType ="Date", value ="最近的节目演出时间")
    private Date recentShowTime;
}
