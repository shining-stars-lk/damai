package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-05
 **/
@Data
@ApiModel(value="ProgramListDto", description ="主页节目列表")
public class ProgramListDto {
    
    @ApiModelProperty(name ="areaId", dataType ="Long", value ="所在区域id")
    @NotNull
    private Long areaId;
    
    @ApiModelProperty(name ="parentProgramCategoryIds", dataType ="Long[]", value ="父节目类型id集合")
    @NotNull
    private List<Long> parentProgramCategoryIds;
    
    /**
     * 业务字段，后端自己填充
     * */
    private Date time;
}
