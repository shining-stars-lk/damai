package com.example.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.example.common.ApiResponse;
import com.example.dto.UserExistDto;
import com.example.dto.UserGetAndTicketUserListDto;
import com.example.dto.UserIdDto;
import com.example.dto.UserMobileDto;
import com.example.dto.UserRegisterDto;
import com.example.dto.UserUpdateDto;
import com.example.service.UserService;
import com.example.vo.CheckVerifyVo;
import com.example.vo.UserGetAndTicketUserListVo;
import com.example.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/
@RestController
@RequestMapping("/user")
@Api(tags = "user", description = "用户")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @ApiOperation(value = "查询(通过手机号)")
    @PostMapping(value = "/getByMobile")
    public ApiResponse<UserVo> getByMobile(@Valid @RequestBody UserMobileDto userMobileDto){
        return ApiResponse.ok(userService.getByMobile(userMobileDto));
    }
    
    @ApiOperation(value = "查询(通过id)")
    @PostMapping(value = "/getById")
    public ApiResponse<UserVo> getById(@Valid @RequestBody UserIdDto userIdDto){
        return ApiResponse.ok(userService.getById(userIdDto));
    }
    
    @ApiOperation(value = "检查是否需要验证码")
    @PostMapping(value = "/check/verify")
    public ApiResponse<CheckVerifyVo> checkVerify(){
        return ApiResponse.ok(userService.checkVerify());
    }
    
    @ApiOperation(value = "获取验证码")
    @PostMapping(value = "/get/captcha/code")
    public ResponseModel getCaptchaCode(@RequestBody CaptchaVO captchaVO){
        return userService.getCaptchaCode(captchaVO);
    }
    
    @ApiOperation(value = "注册")
    @PostMapping(value = "/register")
    public ApiResponse<Void> register(@Valid @RequestBody UserRegisterDto userRegisterDto){
        userService.register(userRegisterDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "是否存在")
    @PostMapping(value = "/exist")
    public ApiResponse<Void> exist(@Valid @RequestBody UserExistDto userExistDto){
        userService.exist(userExistDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "登录")
    @PostMapping(value = "/login")
    public ApiResponse<String> login(@Valid @RequestBody UserMobileDto userMobileDto) {
        return ApiResponse.ok(userService.login(userMobileDto));
    }
    
    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logOut")
    public ApiResponse<Void> logOut(@Valid @RequestBody UserMobileDto userMobileDto) {
        userService.logOut(userMobileDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改个人信息")
    @PostMapping(value = "/update")
    public ApiResponse<Void> update(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.update(userUpdateDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "查询用户和购票人集合")
    @PostMapping(value = "/getUserAndTicketUserList")
    public ApiResponse<UserGetAndTicketUserListVo> getUserAndTicketUserList(@Valid @RequestBody UserGetAndTicketUserListDto userGetAndTicketUserListDto) {
        return ApiResponse.ok(userService.getUserAndTicketUserList(userGetAndTicketUserListDto));
    }
    
    
}
