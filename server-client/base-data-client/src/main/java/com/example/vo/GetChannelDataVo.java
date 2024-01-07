package com.example.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value="GetChannelDataVo", description ="渠道数据")
public class GetChannelDataVo {
    
    @ApiModelProperty(name ="id", dataType ="String", value ="id", required =true)
    private Long id;
    
    @ApiModelProperty(name ="name", dataType ="String", value ="名称", required =true)
    private String name;
    
    @ApiModelProperty(name ="code", dataType ="String", value ="code码", required =true)
    private String code;
    
    @ApiModelProperty(name ="introduce", dataType ="String", value ="介绍", required =true)
    private String introduce;
    
    @ApiModelProperty(name ="createTime", dataType ="Date", value ="创建时间", required =true)
    private Date createTime;
    
    @ApiModelProperty(name ="status", dataType ="Integer", value ="装填 1:正常 0:禁用", required =true)
    private Integer status;
    
    @ApiModelProperty(name ="signPublicKey", dataType ="String", value ="rsa签名公钥", required =true)
    private String signPublicKey;
    
    @ApiModelProperty(name ="signSecretKey", dataType ="String", value ="rsa签名私钥", required =true)
    private String signSecretKey;
    
    @ApiModelProperty(name ="aesKey", dataType ="String", value ="aes私钥")
    private String aesKey;
    
    @ApiModelProperty(name ="dataPublicKey", dataType ="String", value ="rsa参数公钥", required =true)
    private String dataPublicKey;
    
    @ApiModelProperty(name ="dataSecretKey", dataType ="String", value ="rsa参数私钥", required =true)
    private String dataSecretKey;
}