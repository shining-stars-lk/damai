package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 渠道数据 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="GetChannelDataVo", description ="渠道数据")
public class GetChannelDataVo {
    
    @Schema(name ="id", type ="String", description ="id")
    private Long id;
    
    @Schema(name ="name", type ="String", description ="名称")
    private String name;
    
    @Schema(name ="code", type ="String", description ="code码")
    private String code;
    
    @Schema(name ="introduce", type ="String", description ="介绍")
    private String introduce;
    
    @Schema(name ="status", type ="Integer", description ="装填 1:正常 0:禁用")
    private Integer status;
    
    @Schema(name ="signPublicKey", type ="String", description ="rsa签名公钥")
    private String signPublicKey;
    
    @Schema(name ="signSecretKey", type ="String", description ="rsa签名私钥")
    private String signSecretKey;
    
    @Schema(name ="aesKey", type ="String", description ="aes私钥")
    private String aesKey;
    
    @Schema(name ="dataPublicKey", type ="String", description ="rsa参数公钥")
    private String dataPublicKey;
    
    @Schema(name ="dataSecretKey", type ="String", description ="rsa参数私钥")
    private String dataSecretKey;
    
    @Schema(name ="tokenSecret", type ="String", description ="token秘钥")
    private String tokenSecret;
}