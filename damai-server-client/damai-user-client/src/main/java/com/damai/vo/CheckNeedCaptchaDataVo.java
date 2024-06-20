package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 是否需要进行校验验证码 vo
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="CheckNeedCaptchaDataVo", description ="是否需要进行校验验证码")
public class CheckNeedCaptchaDataVo {
    
    @ApiModelProperty(name ="verifyCaptcha", dataType ="Boolean", value ="是否需要验证码 true:是 false:否")
    private Boolean verifyCaptcha;
    
    @ApiModelProperty(name ="id", dataType ="captchaId", value ="唯一标识id，用户注册接口需要传入此id")
    private Long captchaId;
}
