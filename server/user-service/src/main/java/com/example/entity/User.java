package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@Data
@TableName("d_user")
public class User extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名字
     */
    private String name;

    /**
     * 用户真实名字
     */
    private String relName;

    /**
     * 1:男 2:女
     */
    private Integer gender;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 是否邮箱认证 1:已验证 0:未验证
     */
    private Integer mailStatus;

    /**
     * 邮箱地址
     */
    private String mail;

    /**
     * 是否实名认证 1:已验证 0:未验证
     */
    private Integer relAuthenticationStatus;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 登录状态 1登录 0退出
     */
    private Integer loginStatus;
    
    
}
