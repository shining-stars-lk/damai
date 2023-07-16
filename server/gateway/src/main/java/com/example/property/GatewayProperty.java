package com.example.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-03
 **/
@Component
@Data
public class GatewayProperty {
    //需要做频率限制的路径
    @Value("${api.limit.paths:#{null}}")
    private String[] apiRestrictPaths;
    
    @Value("${skip.check.token.paths:/**/test/test,/**/user/login,/**/token/data/add}")
    private String[] skipCheckTokenPaths;
}
