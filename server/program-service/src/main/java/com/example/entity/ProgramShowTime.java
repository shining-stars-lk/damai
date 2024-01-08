package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 节目演出时间表
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Data
@TableName("d_program_show_time")
public class ProgramShowTime extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 节目表id
     */
    private Long programId;

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
