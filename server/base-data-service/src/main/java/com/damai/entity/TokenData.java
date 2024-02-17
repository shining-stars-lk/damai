package com.damai.entity;

import com.damai.data.BaseData;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: token 实体
 * @author: 阿宽不是程序员
 **/
@Data
public class TokenData extends BaseData implements Serializable {
    
    /**
     * id
     * */
    private Long id;
    
    /**
     * 名称
     * */
    private String name;
    
    /**
     * token秘钥
     * */
    private String secret;
}
