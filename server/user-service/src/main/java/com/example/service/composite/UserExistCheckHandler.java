package com.example.service.composite;

import com.example.composite.AbstractComposite;
import com.example.dto.UserRegisterDto;
import com.example.enums.CompositeCheckType;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserExistCheckHandler extends AbstractComposite<UserRegisterDto> {

    @Autowired
    private UserService userService;

    /**
     * 验证是否已注册用户
     * */
    @Override
    public void execute(final UserRegisterDto userRegisterDto) {
        userService.doExist(userRegisterDto.getMobile());
    }

    @Override
    public String type() {
        return CompositeCheckType.USER_REGISTER_CHECK.getValue();
    }
    
    @Override
    public Integer executeParentOrder() {
        return 1;
    }
    
    @Override
    public Integer executeTier() {
        return 2;
    }

    @Override
    public Integer executeOrder() {
        return 1;
    }
}
