package com.damai.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.damai.data.BaseTableData;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 节目分组 实体
 * @author: 阿星不是程序员
 **/
@Data
@TableName("d_program_group")
public class ProgramGroup extends BaseTableData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 节目json
     */
    private String programJson;
    
    /**
     * 最近的节目演出时间
     * */
    private Date recentShowTime;
}
