package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="TicketUserDto", description ="购票人")
public class TicketUserDto implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(name ="userId", dataType ="Long", value ="用户id",required = true)
    @NotNull
    private Long userId;
    
    @ApiModelProperty(name ="relName", dataType ="String", value ="用户真实名字",required = true)
    @NotBlank
    private String relName;
    
    @ApiModelProperty(name ="idType", dataType ="Integer", 
            value ="证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证"
            ,required = true)
    @NotNull
    private Integer idType;
    
    @ApiModelProperty(name ="idNumber", dataType ="String", value ="证件号码",required = true)
    @NotBlank
    private String idNumber;
}
