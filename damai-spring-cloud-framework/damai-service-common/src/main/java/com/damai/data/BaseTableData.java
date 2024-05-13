package com.damai.data;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;
/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 表必要字段
 * @author: 阿星不是程序员
 **/
@Data
public class BaseTableData {

    /**
     * 创建时间
     * */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    
    /**
     * 编辑时间
     * */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date editTime;
    
    
    /**
     * 1:正常 0:删除
     */
    private Integer status;
}
