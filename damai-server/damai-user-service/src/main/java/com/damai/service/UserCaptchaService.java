package com.damai.service;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.core.RedisKeyManage;
import com.damai.service.lua.CheckNeedCaptchaOperate;
import com.damai.service.tool.RequestCounter;
import com.damai.vo.CheckNeedCaptchaDataVo;
import com.damai.vo.CheckVerifyVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: 判断是否需要验证码
 * @author: 阿宽不是程序员
 **/
@Service
public class UserCaptchaService {
    
    @Value("${verify_captcha_threshold:10}")
    private int verifyCaptchaThreshold;
    
    @Autowired
    private CaptchaHandle captchaHandle;
    
    @Autowired
    private RequestCounter requestCounter;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private CheckNeedCaptchaOperate checkNeedCaptchaOperate;
    
    public CheckNeedCaptchaDataVo checkNeedCaptcha() {
        CheckVerifyVo checkVerifyVo = new CheckVerifyVo();
        checkVerifyVo.setType(0);
        List<String> keys = new ArrayList<>();
        keys.add(RedisKeyManage.COUNTER_COUNT.getKey());
        keys.add(RedisKeyManage.COUNTER_TIMESTAMP.getKey());
        keys.add(RedisKeyManage.VERIFY_CAPTCHA_HASH.getKey());
        String[] data = new String[3];
        data[0] = String.valueOf(verifyCaptchaThreshold);
        data[1] = String.valueOf(System.currentTimeMillis());
        data[2] = String.valueOf(uidGenerator.getUid());
        return checkNeedCaptchaOperate.checkNeedCaptchaOperate(keys, data);
    }
    
    public ResponseModel getCaptcha(CaptchaVO captchaVO) {
        return captchaHandle.getCaptcha(captchaVO);
    }
    
    public ResponseModel verifyCaptcha(final CaptchaVO captchaVO) {
        return captchaHandle.checkCaptcha(captchaVO);
    }
}
