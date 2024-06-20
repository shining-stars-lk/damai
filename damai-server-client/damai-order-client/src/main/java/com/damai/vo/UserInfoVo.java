package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户 vo
 * @author: 阿星不是程序员
 **/

@Data
@ApiModel(value="UserInfoVo", description ="用户数据")
public class UserInfoVo {
    
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
    
    @ApiModelProperty(name ="address", dataType ="String", value ="收货地址")
    private String address;
}
