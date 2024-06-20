package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目推荐列表查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ProgramRecommendListDto", description ="节目推荐列表")
public class ProgramRecommendListDto {
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="所在区域id")
    private Long areaId;
    
    @ApiModelProperty(name ="parentProgramCategoryId", dataType ="Long", value ="父节目类型id")
    private Long parentProgramCategoryId;
    
    @ApiModelProperty(name ="programId", dataType ="Long", value ="查看节目详情时，调用推荐列表时要传入此节目id")
    private Long programId;
}
