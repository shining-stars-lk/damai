package com.damai.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户登录 vo
 * @author: 阿星不是程序员
 **/
@Data
@Schema(title="UserLoginVo", description ="用户登录返回实体")
public class UserLoginVo {
    
    @Schema(name ="userId", type ="Long", description ="用户id")
    private Long userId;
    
    @Schema(name ="token", type ="String", description ="token")
    private String token;
}