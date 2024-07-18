package com.damai.service;

import com.damai.captcha.model.common.ResponseModel;
import com.damai.captcha.model.vo.CaptchaVO;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.core.RedisKeyManage;
import com.damai.redis.RedisKeyBuild;
import com.damai.service.lua.CheckNeedCaptchaOperate;
import com.damai.vo.CheckNeedCaptchaDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 判断是否需要验证码
 * @author: 阿星不是程序员
 **/
@Service
public class UserCaptchaService {
    
    @Value("${verify_captcha_threshold:10}")
    private int verifyCaptchaThreshold;
    
    @Value("${verify_captcha_id_expire_time:60}")
    private int verifyCaptchaIdExpireTime;
    
    @Value("${always_verify_captcha:0}")
    private int alwaysVerifyCaptcha;
    
    @Autowired
    private CaptchaHandle captchaHandle;
    
    @Autowired
    private UidGenerator uidGenerator;
    
    @Autowired
    private CheckNeedCaptchaOperate checkNeedCaptchaOperate;
    
    public CheckNeedCaptchaDataVo checkNeedCaptcha() {
        long currentTimeMillis = System.currentTimeMillis();
        long id = uidGenerator.getUid();
        List<String> keys = new ArrayList<>();
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.COUNTER_COUNT).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.COUNTER_TIMESTAMP).getRelKey());
        keys.add(RedisKeyBuild.createRedisKey(RedisKeyManage.VERIFY_CAPTCHA_ID,id).getRelKey());
        String[] data = new String[4];
        data[0] = String.valueOf(verifyCaptchaThreshold);
        data[1] = String.valueOf(currentTimeMillis);
        data[2] = String.valueOf(verifyCaptchaIdExpireTime);
        data[3] = String.valueOf(alwaysVerifyCaptcha);
        Boolean result = checkNeedCaptchaOperate.checkNeedCaptchaOperate(keys, data);
        CheckNeedCaptchaDataVo checkNeedCaptchaDataVo = new CheckNeedCaptchaDataVo();
        checkNeedCaptchaDataVo.setCaptchaId(id);
        checkNeedCaptchaDataVo.setVerifyCaptcha(result);
        return checkNeedCaptchaDataVo;
    }
    
    public ResponseModel getCaptcha(CaptchaVO captchaVO) {
        return captchaHandle.getCaptcha(captchaVO);
    }
    
    public ResponseModel verifyCaptcha(final CaptchaVO captchaVO) {
        return captchaHandle.checkCaptcha(captchaVO);
    }
}
