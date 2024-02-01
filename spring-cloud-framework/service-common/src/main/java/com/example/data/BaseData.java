package com.example.data;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class BaseData {

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
