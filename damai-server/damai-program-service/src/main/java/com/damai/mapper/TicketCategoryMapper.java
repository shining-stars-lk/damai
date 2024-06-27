package com.damai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damai.dto.TicketCategoryCountDto;
import com.damai.entity.TicketCategory;
import com.damai.entity.TicketCategoryAggregate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 票档 mapper
 * @author: 阿星不是程序员
 **/
public interface TicketCategoryMapper extends BaseMapper<TicketCategory> {
    
    /**
     * 票档统计
     * @param programIdList 参数
     * @return 结果
     * */
    List<TicketCategoryAggregate> selectAggregateList(@Param("programIdList")List<Long> programIdList);
    
    /**
     * 更新数量
     * @param number 数量
     * @param id id
     * @return 结果
     * */
    int updateRemainNumber(@Param("number")Long number,@Param("id")Long id);
    
    /**
     * 批量更新数量
     * @param ticketCategoryCountDtoList 参数
     * @param programId 参数
     * @return 结果
     * */
    int batchUpdateRemainNumber(@Param("ticketCategoryCountDtoList") 
                                List<TicketCategoryCountDto> ticketCategoryCountDtoList,
                                @Param("programId")
                                Long programId);
}
