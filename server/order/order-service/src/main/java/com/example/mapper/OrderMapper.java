package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.PsOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
public interface OrderMapper extends BaseMapper<PsOrder> {
    
    @Update({"update ps_order set status = 3 where id = #{id} and status = 0"})
    int timeOutCancelOrder(@Param("id")String id);
}
