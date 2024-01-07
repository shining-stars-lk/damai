package com.example.data;

import lombok.Data;

import java.util.Date;

@Data
public class BaseData {

    /**
     * 创建时间
     * */
    private Date createTime;
    
    /**
     * 编辑时间
     * */
    private Date editTime;
    
    
    /**
     * 1:正常 0:删除
     */
    private Boolean status;
}
