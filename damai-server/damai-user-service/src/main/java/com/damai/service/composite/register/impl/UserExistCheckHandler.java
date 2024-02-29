package com.damai.service.composite.register.impl;

import com.damai.dto.UserRegisterDto;
import com.damai.service.UserService;
import com.damai.service.composite.register.AbstractUserRegisterCheckHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户检查
 * @author: 阿宽不是程序员
 **/
@Component
public class UserExistCheckHandler extends AbstractUserRegisterCheckHandler {

    @Autowired
    private UserService userService;
    
    @Override
    public void execute(final UserRegisterDto userRegisterDto) {
        userService.doExist(userRegisterDto.getMobile());
    }
    
    @Override
    public Integer executeParentOrder() {
        return 1;
    }
    
    @Override
    public Integer executeTier() {
        return 3;
    }

    @Override
    public Integer executeOrder() {
        return 1;
    }
}
