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
@ApiModel(value="GetEmployeeVo", description ="职员")
public class GetEmployeeVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名字", required =true)
    private String name;
    
    @ApiModelProperty(name ="departmentId", dataType ="String", value ="部门id", required =true)
    private String departmentId;
    
    @ApiModelProperty(name ="departmentName", dataType ="String", value ="部门名字", required =true)
    private String departmentName;
}
