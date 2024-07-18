package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseTableData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 用户邮箱 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_user_email")
public class UserEmail extends BaseTableData implements Serializable {

    @Serial
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
     * 邮箱
     */
    private String email;
}
