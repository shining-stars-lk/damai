package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户手机号更新 dto
 * @author: 阿宽不是程序员
 **/
@Data
@ApiModel(value="UserUpdateMobileDto", description ="修改用户手机号")
public class UserUpdateMobileDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(name ="id", dataType ="Long", value ="用户id")
    @NotNull
    private Long id;
    
    @ApiModelProperty(name ="mobile", dataType ="String", value ="手机号")
    @NotBlank
    private String mobile;
    
}
