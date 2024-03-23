package com.damai.balance.config;

import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 灰度版本选择相关配置
 * @author: 阿宽不是程序员
 **/

@RibbonClients(defaultConfiguration = { WorkLoadBalanceConfiguration.class })
public class ExtraRibbonAutoConfiguration {
}
