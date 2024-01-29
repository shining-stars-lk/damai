package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.OrderTicketUser;
import com.example.entity.OrderTicketUserAggregate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购票人订单表 Mapper 接口
 * </p>
 *
 * @author k
 * @since 2024-01-12
 */
public interface OrderTicketUserMapper extends BaseMapper<OrderTicketUser> {
    
    List<OrderTicketUserAggregate> selectOrderTicketUserAggregate(@Param("orderIdList")List<Long> orderIdList);

}
