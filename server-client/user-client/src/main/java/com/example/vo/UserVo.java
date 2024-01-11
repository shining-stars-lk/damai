package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/

@Data
@ApiModel(value="UserVo", description ="用户数据")
public class UserVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="用户id")
    private Long id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户名字")
    private String name;
    
    @ApiModelProperty(name ="relName", dataType ="String", value ="用户真实名字")
    private String relName;
    
    @ApiModelProperty(name ="gender", dataType ="Integer", value ="1:男 2:女")
    private Integer gender;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="用户手机号")
    private String mobile;
    
    @ApiModelProperty(name ="mailStatus", dataType ="Integer", value ="是否邮箱认证 1:已验证 0:未验证")
    private Integer mailStatus;
    
    @ApiModelProperty(name ="mail", dataType ="String", value ="邮箱地址")
    private String mail;
    
    @ApiModelProperty(name ="relAuthenticationStatus", dataType ="Integer", value ="是否实名认证 1:已验证 0:未验证")
    private Integer relAuthenticationStatus;
    
    @ApiModelProperty(name ="idNumber", dataType ="String", value ="身份证号码")
    private String idNumber;
    
    @ApiModelProperty(name ="address", dataType ="String", value ="收货地址")
    private String address;
}
