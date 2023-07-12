package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-05
 **/

@Data
@ApiModel(value="GetDeptVo", description ="GetDeptVo")
public class GetDeptVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名字")
    private String name;
    
    @ApiModelProperty(name ="typeCode", dataType ="String", value ="类型编码", required =true)
    private String typeCode;
    
    
    @ApiModelProperty(name ="createTime", dataType ="Date", value ="创建日期", required =true)
    private Date createTime;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="状态 1生效 0禁用", required =true)
    private Integer status;
}
