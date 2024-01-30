package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户手机表
 * </p>
 *
 * @author k
 * @since 2024-01-30
 */
@Data
@TableName("d_user_mobile")
public class UserMobile extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 手机号
     */
    private String mobile;
}
