package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Data
@ApiModel(value="JobInfoDto", description ="job任务")
public class JobInfoDto {
    
    @ApiModelProperty(name ="name", dataType ="String", value ="job任务名字", required =true)
    @NotBlank
    private String name;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="job任务描述")
    private String description;
    
    @ApiModelProperty(name ="url", dataType ="String", value ="job任务路径", required =true)
    @NotBlank
    private String url;
    
    @ApiModelProperty(name ="headers", dataType ="String", value ="job任务请求头信息", required =true)
    @NotBlank
    private String headers;
    
    @ApiModelProperty(name ="method", dataType ="Integer", value ="job任务方法 1:get 2:post 3:put", required =true)
    @NotNull
    private Integer method;
    
    @ApiModelProperty(name ="params", dataType ="String", value ="job任务请求参数")
    private String params;
    
    @ApiModelProperty(name ="retry", dataType ="Integer", value ="是否重试 1是 0否")
    private Integer retry;
    
    @ApiModelProperty(name ="retryNumber", dataType ="Integer", value ="重试次数")
    private Integer retryNumber;
}
