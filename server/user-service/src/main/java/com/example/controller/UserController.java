package com.example.controller;

import com.example.common.ApiResponse;
import com.example.dto.UserAuthenticationDto;
import com.example.dto.UserExistDto;
import com.example.dto.UserGetAndTicketUserListDto;
import com.example.dto.UserIdDto;
import com.example.dto.UserLoginDto;
import com.example.dto.UserMobileDto;
import com.example.dto.UserRegisterDto;
import com.example.dto.UserUpdateDto;
import com.example.dto.UserUpdateEmailDto;
import com.example.dto.UserUpdateMobileDto;
import com.example.dto.UserUpdatePasswordDto;
import com.example.service.UserService;
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
    @PostMapping(value = "/get/mobile")
    public ApiResponse<UserVo> getByMobile(@Valid @RequestBody UserMobileDto userMobileDto){
        return ApiResponse.ok(userService.getByMobile(userMobileDto));
    }
    
    @ApiOperation(value = "查询(通过id)")
    @PostMapping(value = "/get/id")
    public ApiResponse<UserVo> getById(@Valid @RequestBody UserIdDto userIdDto){
        return ApiResponse.ok(userService.getById(userIdDto));
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
    public ApiResponse<String> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return ApiResponse.ok(userService.login(userLoginDto));
    }
    
    @ApiOperation(value = "退出登录")
    @PostMapping(value = "/logout")
    public ApiResponse<Void> logout(@Valid @RequestBody UserIdDto userIdDto) {
        userService.logout(userIdDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改个人信息")
    @PostMapping(value = "/update")
    public ApiResponse<Void> update(@Valid @RequestBody UserUpdateDto userUpdateDto) {
        userService.update(userUpdateDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改密码")
    @PostMapping(value = "/update/password")
    public ApiResponse<Void> updatePassword(@Valid @RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        userService.updatePassword(userUpdatePasswordDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改邮箱")
    @PostMapping(value = "/update/email")
    public ApiResponse<Void> updateEmail(@Valid @RequestBody UserUpdateEmailDto userUpdateEmailDto) {
        userService.updateEmail(userUpdateEmailDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "修改手机号")
    @PostMapping(value = "/update/mobile")
    public ApiResponse<Void> updateMobile(@Valid @RequestBody UserUpdateMobileDto userUpdateMobileDto) {
        userService.updateMobile(userUpdateMobileDto);
        return ApiResponse.ok();
    }
    
    @ApiOperation(value = "实名认证")
    @PostMapping(value = "/authentication")
    public ApiResponse<Void> authentication(@Valid @RequestBody UserAuthenticationDto userAuthenticationDto) {
        userService.authentication(userAuthenticationDto);
        return ApiResponse.ok();
    }
    
    
    @ApiOperation(value = "查询用户和购票人集合(只提供给服务内部调用，不提供给前端)")
    @PostMapping(value = "/get/user/ticket/list")
    public ApiResponse<UserGetAndTicketUserListVo> getUserAndTicketUserList(@Valid @RequestBody UserGetAndTicketUserListDto userGetAndTicketUserListDto) {
        return ApiResponse.ok(userService.getUserAndTicketUserList(userGetAndTicketUserListDto));
    }
    
    
}
