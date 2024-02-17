package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Data
@ApiModel(value="JobInfoDtoPage", description ="job任务查询")
public class JobInfoDtoPage {
    
    @ApiModelProperty(name ="pageSize", dataType ="Integer", value ="页码", required =true)
    @NotNull
    private Integer pageSize;
    
    @ApiModelProperty(name ="pageNo", dataType ="Integer", value ="页容量", required =true)
    @NotNull
    private Integer pageNo;
}
