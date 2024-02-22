package com.damai.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 渠道数据 vo
 * @author: 阿宽不是程序员
 **/
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
    
    @ApiModelProperty(name ="tokenSecret", dataType ="String", value ="token秘钥", required =true)
    private String tokenSecret;
}