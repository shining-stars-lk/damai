package com.damai.pro.limit;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 线上限流工具属性
 * @author: 阿星不是程序员
 **/
@Data
public class RateLimiterProperty {
    
    @Value("${rate.switch:false}")
    private Boolean rateSwitch;

    @Value("${rate.permits:200}")
    private Integer ratePermits;
}
