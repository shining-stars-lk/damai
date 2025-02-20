package com.damai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damai.entity.OrderTicketUser;
import com.damai.entity.OrderTicketUserAggregate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 购票人订单 mapper
 * @author: 阿星不是程序员
 **/
public interface OrderTicketUserMapper extends BaseMapper<OrderTicketUser> {
    
    /**
     * 查询订单下购票人数量
     * @param orderNumberList 参数
     * @return 结果
     * */
    List<OrderTicketUserAggregate> selectOrderTicketUserAggregate(@Param("orderNumberList")List<Long> orderNumberList);
    
    /**
     * 真实删除购票人订单数据
     * @return 结果
     * */
    Integer relDelOrderTicketUser();

}
