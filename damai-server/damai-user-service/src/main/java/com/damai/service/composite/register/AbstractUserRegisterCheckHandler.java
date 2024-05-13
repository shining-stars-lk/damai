package com.damai.service.composite.register;


import com.damai.dto.UserRegisterDto;
import com.damai.enums.CompositeCheckType;
import com.damai.initialize.impl.composite.AbstractComposite;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户注册验证基类，用户注册的相关验证逻辑继承此类
 * @author: 阿星不是程序员
 **/
public abstract class AbstractUserRegisterCheckHandler extends AbstractComposite<UserRegisterDto> {
    
    @Override
    public String type() {
        return CompositeCheckType.USER_REGISTER_CHECK.getValue();
    }
}
