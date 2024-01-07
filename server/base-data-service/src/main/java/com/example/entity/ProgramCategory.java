package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author k
 * @since 2024-01-07
 */
@Data
@TableName("d_program_category")
public class ProgramCategory extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 区域id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父区域id
     */
    private Long parentId;

    /**
     * 区域名字
     */
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    private Integer type;
}
