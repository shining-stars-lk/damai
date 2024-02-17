package com.damai.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.damai.common.ApiResponse;
import com.damai.service.UserCaptchaService;
import com.damai.vo.CheckVerifyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/user/captcha")
@Api(tags = "captcha", description = "验证码")
public class UserCaptchaController {
    
    @Autowired
    private UserCaptchaService userCaptchaService;
    
    @ApiOperation(value = "检查是否需要验证码")
    @PostMapping(value = "/check/need")
    public ApiResponse<CheckVerifyVo> checkNeedCaptcha(){
        return ApiResponse.ok(userCaptchaService.checkNeedCaptcha());
    }
    
    @ApiOperation(value = "获取验证码")
    @PostMapping(value = "/get")
    public ResponseModel getCaptcha(@RequestBody CaptchaVO captchaVO){
        return userCaptchaService.getCaptcha(captchaVO);
    }
    
    @ApiOperation(value = "验证验证码")
    @PostMapping(value = "/verify")
    public ResponseModel verifyCaptcha(@RequestBody CaptchaVO captchaVO){
        return userCaptchaService.verifyCaptcha(captchaVO);
    }
}
