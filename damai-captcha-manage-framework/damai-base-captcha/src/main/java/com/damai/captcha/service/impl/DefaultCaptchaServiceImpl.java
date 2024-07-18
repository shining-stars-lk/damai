/*
 *Copyright © 2018 anji-plus
 *安吉加加信息技术有限公司
 *http://www.anji-plus.com
 *All rights reserved.
 */
package com.damai.captcha.service.impl;

import com.damai.captcha.model.common.RepCodeEnum;
import com.damai.captcha.model.common.ResponseModel;
import com.damai.captcha.model.vo.CaptchaVO;
import com.damai.captcha.service.CaptchaService;
import com.damai.captcha.util.StringUtils;

import java.util.Properties;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 默认实现
 * @author: 阿星不是程序员
 **/
public class DefaultCaptchaServiceImpl extends AbstractCaptchaService{

    @Override
    public String captchaType() {
        return "default";
    }

    @Override
    public void init(Properties config) {
        for (String s : CaptchaServiceFactory.instances.keySet()) {
            if(captchaType().equals(s)){
                continue;
            }
            getService(s).init(config);
        }
    }

	@Override
	public void destroy(Properties config) {
		for (String s : CaptchaServiceFactory.instances.keySet()) {
			if(captchaType().equals(s)){
				continue;
			}
			getService(s).destroy(config);
		}
	}

	private CaptchaService getService(String captchaType){
        return CaptchaServiceFactory.instances.get(captchaType);
    }

    @Override
    public ResponseModel get(CaptchaVO captchaVO) {
        if (captchaVO == null) {
            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
        }
        if (StringUtils.isEmpty(captchaVO.getCaptchaType())) {
            return RepCodeEnum.NULL_ERROR.parseError("类型");
        }
        return getService(captchaVO.getCaptchaType()).get(captchaVO);
    }

    @Override
    public ResponseModel check(CaptchaVO captchaVO) {
        if (captchaVO == null) {
            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
        }
        if (StringUtils.isEmpty(captchaVO.getCaptchaType())) {
            return RepCodeEnum.NULL_ERROR.parseError("类型");
        }
        if (StringUtils.isEmpty(captchaVO.getToken())) {
            return RepCodeEnum.NULL_ERROR.parseError("token");
        }
        return getService(captchaVO.getCaptchaType()).check(captchaVO);
    }

    @Override
    public ResponseModel verification(CaptchaVO captchaVO) {
        if (captchaVO == null) {
            return RepCodeEnum.NULL_ERROR.parseError("captchaVO");
        }
        if (StringUtils.isEmpty(captchaVO.getCaptchaVerification())) {
            return RepCodeEnum.NULL_ERROR.parseError("二次校验参数");
        }
        try {
            String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, captchaVO.getCaptchaVerification());
            if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
                return ResponseModel.errorMsg(RepCodeEnum.API_CAPTCHA_INVALID);
            }
            //二次校验取值后，即刻失效
            CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        } catch (Exception e) {
            logger.error("验证码坐标解析失败", e);
            return ResponseModel.errorMsg(e.getMessage());
        }
        return ResponseModel.success();
    }

}
