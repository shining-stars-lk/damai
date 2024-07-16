package com.damai.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 渠道数据添加 dto
 * @author: 阿星不是程序员
 **/
@Data
@ApiModel(value="ChannelDataAddDto", description ="渠道数据")
public class ChannelDataAddDto {
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名称", required =true)
    @NotBlank
    private String name;
    
    @ApiModelProperty(name ="code", dataType ="String", value ="code码", required =true)
    @NotBlank
    private String code;
    
    @ApiModelProperty(name ="introduce", dataType ="String", value ="介绍", required =true)
    private String introduce;
    
    @ApiModelProperty(name ="signPublicKey", dataType ="String", value ="rsa签名公钥", required =true)
    @NotBlank
    private String signPublicKey;
    
    @ApiModelProperty(name ="signSecretKey", dataType ="String", value ="rsa签名私钥", required =true)
    @NotBlank
    private String signSecretKey;
    
    @ApiModelProperty(name ="aesKey", dataType ="String", value ="aes私钥")
    private String aesKey;
    
    @ApiModelProperty(name ="dataPublicKey", dataType ="String", value ="rsa参数公钥")
    private String dataPublicKey;
    
    @ApiModelProperty(name ="dataSecretKey", dataType ="String", value ="rsa参数私钥")
    private String dataSecretKey;
    
    @ApiModelProperty(name ="tokenSecret", dataType ="String", value ="token秘钥", required =true)
    @NotBlank
    private String tokenSecret;;
    
}
