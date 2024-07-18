package com.damai.service;

import com.damai.captcha.model.common.ResponseModel;
import com.damai.captcha.model.vo.CaptchaVO;
import com.damai.captcha.service.CaptchaService;
import com.damai.util.RemoteUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 验证码处理器
 * @author: 阿星不是程序员
 **/
@AllArgsConstructor
public class CaptchaHandle {
    
    private final CaptchaService captchaService;
    
    public ResponseModel getCaptcha(CaptchaVO captchaVO) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        captchaVO.setBrowserInfo(RemoteUtil.getRemoteId(request));
        return captchaService.get(captchaVO);
    }
    
    public ResponseModel checkCaptcha(CaptchaVO captchaVO) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        captchaVO.setBrowserInfo(RemoteUtil.getRemoteId(request));
        return captchaService.check(captchaVO);
    }
    
    public ResponseModel verification(CaptchaVO captchaVO) {
        return captchaService.verification(captchaVO);
    }
}
