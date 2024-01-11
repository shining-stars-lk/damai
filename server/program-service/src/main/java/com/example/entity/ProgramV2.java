package com.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 节目表(连表查询使用)
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Data
public class ProgramV2 extends Program implements Serializable {

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
