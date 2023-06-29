package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.dto.ApiDataDto;
import com.example.entity.ApiData;
import com.example.vo.ApiDataVo;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-14
 **/
public interface ApiDataMapper extends BaseMapper<ApiData> {
    @Select({
        "<script>",
            "select",
            "id,head_version,api_address,api_method,api_body,api_params,api_url,create_time",
            "status,call_day_time,call_hour_time,call_minute_time,call_second_time,count(1) as statistics",
            "from api_data",
            "where status = 1",
            "<if test='startDate != null and startDate != \"\"'>  and create_time <![CDATA[ >= ]]> #{startDate} </if>",
            "<if test='endDate != null and endDate != \"\"'>  and create_time <![CDATA[ <= ]]> #{endDate} </if>",
            "<if test='apiAddress != null and apiAddress != \"\"'>  and api_address like concat(#{apiAddress},'%') </if>",
            "<if test='apiUrl != null and apiUrl != \"\"'>  and api_url like concat(#{apiUrl},'%') </if>",
            "group by api_address, api_url",
            "order by create_time desc",
        "</script>",    
    })
    @Results({
        @Result(column="id", property="id", jdbcType= JdbcType.VARCHAR),
        @Result(column="head_version", property="headVersion", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_address", property="apiAddress", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_method", property="apiMethod", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_body", property="apiBody", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_params", property="apiParams", jdbcType=JdbcType.VARCHAR),
        @Result(column="api_url", property="apiUrl", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="call_day_time", property="callDayTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="call_hour_time", property="callHourTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="call_minute_time", property="callMinuteTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="call_second_time", property="callSecondTime", jdbcType=JdbcType.VARCHAR)
    })
    Page<ApiDataVo> pageList(Page<ApiData> page, ApiDataDto apiDataDto);
}
