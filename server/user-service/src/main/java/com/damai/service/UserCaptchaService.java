package com.damai.service;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.damai.service.tool.RequestCounter;
import com.damai.vo.CheckVerifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2024-02-06
 **/
@Service
public class UserCaptchaService {
    
    @Autowired
    private CaptchaHandle captchaHandle;
    
    @Autowired
    private RequestCounter requestCounter;
    
    public CheckVerifyVo checkNeedCaptcha() {
        CheckVerifyVo checkVerifyVo = new CheckVerifyVo();
        checkVerifyVo.setType(0);
        boolean result = requestCounter.onRequest();
        if (result) {
            checkVerifyVo.setType(1);
        }
        return checkVerifyVo;
    }
    
    public ResponseModel getCaptcha(CaptchaVO captchaVO) {
        return captchaHandle.getCaptcha(captchaVO);
    }
    
    public ResponseModel verifyCaptcha(final CaptchaVO captchaVO) {
        return captchaHandle.checkCaptcha(captchaVO);
    }
}
