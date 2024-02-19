package com.damai.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 配置属性
 * @author: 阿宽不是程序员
 **/
@Component
@Data
public class GatewayProperty {
    /**
     * 需要做频率限制的路径
     */
    @Value("${api.limit.paths:#{null}}")
    private String[] apiRestrictPaths;
    
    @Value("${skip.check.token.paths:/**/test/test,/**/user/login,/**/token/data/add}")
    private String[] skipCheckTokenPaths;
}
