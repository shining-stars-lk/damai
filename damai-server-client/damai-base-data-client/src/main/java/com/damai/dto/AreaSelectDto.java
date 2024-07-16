package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 地区列表查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="AreaSelectDto", description ="AreaSelectDto")
public class AreaSelectDto {
    
    @ApiModelProperty(name ="idList", dataType ="List<Long>", value ="id集合", required =true)
    @NotNull
    private List<Long> idList;
}
