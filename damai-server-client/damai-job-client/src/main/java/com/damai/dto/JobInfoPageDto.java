package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job任务查询 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="JobInfoPageDto", description ="job任务查询")
public class JobInfoPageDto {
    
    @ApiModelProperty(name ="pageSize", dataType ="Integer", value ="页码", required =true)
    @NotNull
    private Integer pageSize;
    
    @ApiModelProperty(name ="pageNo", dataType ="Integer", value ="页容量", required =true)
    @NotNull
    private Integer pageNo;
}
