package com.damai.entity;

import com.damai.data.BaseData;
import lombok.Data;

import java.io.Serializable;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 渠道 实体
 * @author: 阿宽不是程序员
 **/
@Data
public class ChannelData extends BaseData implements Serializable {
    /**
     * id
     * */
    private Long id;
    
    /**
     * 名称
     * */
    private String name;
    
    /**
     * 编码
     * */
    private String code;
    
    /**
     * 介绍描述
     * */
    private String introduce;
    
    /**
     * rsa签名公钥
     * */
    private String signPublicKey;
    
    /**
     * rsa签名秘钥
     * */
    private String signSecretKey;
    
    /**
     * aes秘钥
     * */
    private String aesKey;
    
    /**
     * rsa参数公钥
     * */
    private String dataPublicKey;
    
    /**
     * rsa参数私钥
     * */
    private String dataSecretKey;
    
    /**
     * token秘钥
     * */
    private String tokenSecret;
    
}