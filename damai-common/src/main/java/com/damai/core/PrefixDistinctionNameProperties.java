package com.damai.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.damai.core.PrefixDistinctionNameProperties.PREFIX;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 前缀唯一区分
 * @author: 阿宽不是程序员
 **/
@Data
@ConfigurationProperties(prefix = PREFIX)
public class PrefixDistinctionNameProperties {
    
    public static final String PREFIX = "prefix.distinction";
    
    private String name = "damai";
}
