package com.damai.service.composite.register.impl;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.damai.core.RedisKeyManage;
import com.damai.core.StringUtil;
import com.damai.dto.UserRegisterDto;
import com.damai.enums.BaseCode;
import com.damai.enums.VerifyCaptcha;
import com.damai.exception.DaMaiFrameException;
import com.damai.redis.RedisCache;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.CaptchaHandle;
import com.damai.service.composite.register.AbstractUserRegisterCheckHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 用户注册检查
 * @author: 阿宽不是程序员
 **/
@Component
public class UserRegisterVerifyCaptcha extends AbstractUserRegisterCheckHandler {
    
    @Autowired
    private CaptchaHandle captchaHandle;
    
    @Autowired
    private RedisCache redisCache;
    
    /**
     * 验证验证码是否正确
     * */
    @Override
    protected void execute(UserRegisterDto param) {
        String verifyCaptcha = redisCache.getForHash(RedisKeyBuild.createRedisKey(RedisKeyManage.VERIFY_CAPTCHA_HASH),
                RedisKeyBuild.createRedisKey(RedisKeyManage.VERIFY_CAPTCHA_ID,param.getCaptchaId()).getRelKey(), String.class);
        if (StringUtil.isEmpty(verifyCaptcha)) {
            throw new DaMaiFrameException(BaseCode.VERIFY_CAPTCHA_ID_NOT_EXIST);
        }
        if (VerifyCaptcha.YES.getValue().equals(verifyCaptcha)) {
            if (StringUtil.isEmpty(param.getCaptchaType())) {
                throw new DaMaiFrameException(BaseCode.CAPTCHA_TYPE_EMPTY);
            }
            if (StringUtil.isEmpty(param.getPointJson())) {
                throw new DaMaiFrameException(BaseCode.POINT_JSON_EMPTY);
            }
            if (StringUtil.isEmpty(param.getToken())) {
                throw new DaMaiFrameException(BaseCode.CAPTCHA_TOKEN_JSON_EMPTY);
            }
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaType(param.getCaptchaType());
            captchaVO.setPointJson(param.getPointJson());
            captchaVO.setToken(param.getToken());
            ResponseModel responseModel = captchaHandle.checkCaptcha(captchaVO);
            if (!responseModel.isSuccess()) {
                throw new DaMaiFrameException(responseModel.getRepCode(),responseModel.getRepMsg());
            }
        }
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
