package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户登录 vo
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="UserLoginVo", description ="用户登录返回实体")
public class UserLoginVo {
    
    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id")
    private Long userId;
    
    @ApiModelProperty(name ="token", dataType ="String", value ="token")
    private String token;
}