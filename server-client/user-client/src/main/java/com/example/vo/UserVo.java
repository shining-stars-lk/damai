package com.example.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/

@Data
public class UserVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="用户id", required =true)
    private String id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户名字", required =true)
    private String name;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户手机号", required =true)
    private String mobile;
    
    @ApiModelProperty(name ="password", dataType ="String", value ="用户密码", required =true)
    private String password;
    
    @ApiModelProperty(name ="age", dataType ="Integer", value ="用户年龄", required =true)
    private Integer age;
    
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="状态 1正常 0禁用", required =true)
    private Integer status;
    
    @ApiModelProperty(name ="createTime", dataType ="Date", value ="创建时间", required =true)
    private Date createTime;
}
