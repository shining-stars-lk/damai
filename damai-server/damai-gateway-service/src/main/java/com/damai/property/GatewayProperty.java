package com.damai.property;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 配置属性
 * @author: 阿星不是程序员
 **/
@Data
@Component
public class GatewayProperty {
    /**
     * 需要做频率限制的路径
     */
    @Value("${api.limit.paths:#{null}}")
    private String[] apiRestrictPaths;
    
    @Value("${skip.check.token.paths:/**/program/order/create/v1,/**/program/order/create/v2,/**/program/order/create/v3," +
            "/**/program/order/create/v4,/**/ticket/user/add,/**/ticket/user/delete,/**/ticket/user/list,/**/user/authentication," +
            "/**/user/update,/**/user/update/email,/**/user/update/mobile,/**/user/update/password," +
            "/**/order/cancel,/**/order/create,/**/order/pay,/**/order/select/list,/**/order/get,/**/order/cancel}")
    private String[] checkTokenPaths;
    
    @Value("${skip.check.parmeter.paths:/**/alipay/notify}")
    private String[] checkSkipParmeterPaths;
    
    @Value("${allow.normal.access:true}")
    private boolean allowNormalAccess;
    
    @Value("${userId.paths:/**/program/detail,/**/program/detail/v1,/**/program/detail/v2}")
    private String[] userIdPaths;
}
