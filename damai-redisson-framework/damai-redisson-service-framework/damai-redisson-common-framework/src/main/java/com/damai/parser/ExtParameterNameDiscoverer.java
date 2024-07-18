package com.damai.parser;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.NativeDetector;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 对DefaultParameterNameDiscoverer进行扩展，添加{@link LocalVariableTableParameterNameDiscoverer}
 * @author: 阿星不是程序员
 **/
public class ExtParameterNameDiscoverer extends DefaultParameterNameDiscoverer {
    
    public ExtParameterNameDiscoverer() {
        super();
        if (!NativeDetector.inNativeImage()) {
            addDiscoverer(new LocalVariableTableParameterNameDiscoverer());
        }
    }
}
