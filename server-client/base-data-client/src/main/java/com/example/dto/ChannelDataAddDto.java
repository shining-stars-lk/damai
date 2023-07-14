package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-07-04
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
    
    @ApiModelProperty(name ="publicKey", dataType ="String", value ="rsa公钥", required =true)
    @NotBlank
    private String publicKey;
    
    @ApiModelProperty(name ="secretKey", dataType ="String", value ="rsa私钥", required =true)
    @NotBlank
    private String secretKey;
    
    @ApiModelProperty(name ="aesKey", dataType ="String", value ="aes私钥")
    private String aesKey;
    
}
