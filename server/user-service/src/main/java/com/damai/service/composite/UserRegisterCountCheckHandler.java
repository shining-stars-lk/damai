package com.damai.service.composite;

import com.damai.composite.AbstractComposite;
import com.damai.dto.UserRegisterDto;
import com.damai.enums.BaseCode;
import com.damai.enums.CompositeCheckType;
import com.damai.exception.DaMaiFrameException;
import com.damai.service.tool.RequestCounter;
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
    
    /**
     * 验证每秒用户注册请求是否超过最大限制
     * */
    @Override
    protected void execute(final UserRegisterDto param) {
        boolean result = requestCounter.onRequest();
        if (result) {
            throw new DaMaiFrameException(BaseCode.USER_REGISTER_FREQUENCY);
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
