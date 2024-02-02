package com.example.service.composite;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.example.composite.AbstractComposite;
import com.example.core.StringUtil;
import com.example.dto.UserRegisterDto;
import com.example.enums.CompositeCheckType;
import com.example.exception.CookFrameException;
import com.example.service.CaptchaHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-01
 **/
@Component
public class UserRegisterVerifyCaptcha extends AbstractComposite<UserRegisterDto> {
    
    @Autowired
    private CaptchaHandle captchaHandle;
    
    /**
     * 验证验证码是否正确
     * */
    @Override
    protected void execute(UserRegisterDto param) {
        if (StringUtil.isNotEmpty(param.getCaptchaType())) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaType(param.getCaptchaType());
            captchaVO.setPointJson(param.getPointJson());
            captchaVO.setToken(param.getToken());
            ResponseModel responseModel = captchaHandle.checkCaptcha(captchaVO);
            if (!responseModel.isSuccess()) {
                throw new CookFrameException(responseModel.getRepCode(),responseModel.getRepMsg());
            }
        }
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
        return 3;
    }
    
    @Override
    public Integer executeOrder() {
        return 1;
    }
}
