package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value="UserDto", description ="用户")
public class UserDto {
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户名字", required =true)
    @NotBlank
    private String name;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户手机号", required =true)
    @NotBlank
    private String mobile;
    
    @ApiModelProperty(name ="password", dataType ="String", value ="用户密码", required =true)
    private String password;
    
    @ApiModelProperty(name ="age", dataType ="Integer", value ="用户年龄", required =true)
    private Integer age;
    
    @ApiModelProperty(name ="code", dataType ="String", value ="平台code", required =true)
    @NotBlank
    private String code;
}