package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.data.BaseData;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 节目票档与节目关联表
 * </p>
 *
 * @author k
 * @since 2024-01-08
 */
@Data
@TableName("d_ticket_category_relation")
public class TicketCategoryRelation extends BaseData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 节目id
     */
    private Long programId;

    /**
     * 节目票档id
     */
    private Long ticketCategoryId;
}
