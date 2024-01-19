package com.example.service.composite;

import com.example.composite.AbstractComposite;
import com.example.dto.UserRegisterDto;
import com.example.enums.BaseCode;
import com.example.enums.CompositeCheckType;
import com.example.exception.CookFrameException;
import com.example.service.tool.RequestCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-01-19
 **/
@Component
public class UserRegisterCountCheckHandler extends AbstractComposite<UserRegisterDto> {
    
    @Autowired
    private RequestCounter requestCounter;
    
    @Override
    protected void execute(final UserRegisterDto param) {
        boolean result = requestCounter.onRequest();
        if (result) {
            throw new CookFrameException(BaseCode.USER_REGISTER_FREQUENCY);
        }
    }
    
    @Override
    public String type() {
        return CompositeCheckType.USER_REGISTER_CHECK.getValue();
    }
    
    @Override
    public Integer executeParentOrder() {
        return 0;
    }
    
    @Override
    public Integer executeTier() {
        return 1;
    }
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
}
