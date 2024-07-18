package com.damai.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目 实体 连表使用
 * @author: 阿星不是程序员
 **/
@Data
public class ProgramJoinShowTime extends Program implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 演出时间
     */
    private Date showTime;
    
    /**
     * 演出时间(精确到天)
     */
    private Date showDayTime;
    
    /**
     * 演出时间所在的星期
     */
    private String showWeekTime;
}
