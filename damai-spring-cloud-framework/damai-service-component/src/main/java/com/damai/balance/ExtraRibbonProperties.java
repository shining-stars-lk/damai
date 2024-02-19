package com.damai.balance;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 灰度标识
 * @author: 阿宽不是程序员
 **/
@ConfigurationProperties("spring.cloud.nacos.discovery.metadata")
@Data
public class ExtraRibbonProperties {
    
    private String mark;
}
