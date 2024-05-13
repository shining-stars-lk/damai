package com.damai.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import static com.damai.constant.Constant.SPRING_INJECT_PREFIX_DISTINCTION_NAME;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 前缀唯一区分
 * @author: 阿星不是程序员
 **/
@Data
public class PrefixDistinctionNameProperties {
    
    @Value(SPRING_INJECT_PREFIX_DISTINCTION_NAME)
    private String name;
}
