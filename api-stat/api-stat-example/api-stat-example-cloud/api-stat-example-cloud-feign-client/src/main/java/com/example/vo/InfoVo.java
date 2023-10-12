package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value="InfoVo", description ="信息数据")
@Data
public class InfoVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名称", required =true)
    private String name;
    
    @ApiModelProperty(name ="code", dataType ="String", value ="code码", required =true)
    private String code;
}