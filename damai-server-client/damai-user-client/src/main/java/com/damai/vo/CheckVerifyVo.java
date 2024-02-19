package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 检查验证码 vo
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="CheckVerifyVo", description ="检查验证码")
public class CheckVerifyVo {
    
    @ApiModelProperty(name ="type", dataType ="Integer", value ="是否需要验证码 1:是 0:否")
    private Integer type;
}
