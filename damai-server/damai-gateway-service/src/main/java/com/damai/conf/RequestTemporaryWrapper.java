package com.damai.conf;

import com.damai.common.ApiResponse;
import lombok.Data;

import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 临时信息
 * @author: 阿星不是程序员
 **/
@Data
public class RequestTemporaryWrapper {
    
    private Map<String,String> map;
    
    private ApiResponse<?> apiResponse;
}
