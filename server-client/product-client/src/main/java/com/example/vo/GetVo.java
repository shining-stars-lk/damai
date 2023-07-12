package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-04-17
 **/
@Data
@ApiModel(value="GetVo", description ="产品")
public class GetVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="产品id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="产品名字", required =true)
    private String name;
    
    @ApiModelProperty(name ="number", dataType ="Integer", value ="产品数量", required =true)
    private Integer number;
}
