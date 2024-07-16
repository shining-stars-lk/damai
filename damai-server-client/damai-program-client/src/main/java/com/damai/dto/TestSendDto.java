package com.damai.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: test dto
 * @author: 阿星不是程序员
 **/
@Data
public class TestSendDto {
    
    private Long count;
    
    @ApiModelProperty(name ="message", dataType ="String", value ="消息",required = true)
    @NotBlank
    private String message;
    
    private Long time;
}
