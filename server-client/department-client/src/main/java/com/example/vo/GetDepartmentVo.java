package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@Data
@ApiModel(value="GetDepartmentVo", description ="部门")
public class GetDepartmentVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名字")
    private String name;
}
