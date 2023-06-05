package com.example.mapper;

import com.example.dto.PayDto;
import com.example.entity.Pay;
import com.example.provider.PayProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface PayMapper {
   

    @InsertProvider(type= PayProvider.class, method="insert")
    int insert(Pay pay);

    @Select({
        "select",
        "id, user_id, type, status, amount, origin, pay_time, department_id, create_time, ",
        "refund_status, refund_amount, refund_time, refund_mark",
        "from pay",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @Results({
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
        @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
        @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
        @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
        @Result(column="origin", property="origin", jdbcType=JdbcType.INTEGER),
        @Result(column="pay_time", property="payTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="department_id", property="departmentId", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="refund_status", property="refundStatus", jdbcType=JdbcType.INTEGER),
        @Result(column="refund_amount", property="refundAmount", jdbcType=JdbcType.DECIMAL),
        @Result(column="refund_time", property="refundTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="refund_mark", property="refundMark", jdbcType=JdbcType.VARCHAR)
    })
    Pay getById(Long id);
    
    @Select({
        "<script>",
            "select",
            "id, user_id, type, status, amount, origin, pay_time, department_id, create_time, ",
            "refund_status, refund_amount, refund_time, refund_mark",
            "from pay",
            "where 1 = 1",
            "<if test='userId != null and userId !=\"\"'>",
                "and user_id = #{userId,jdbcType=VARCHAR}",
            "</if>",
            "<if test='type != null and type !=\"\"'>",
                "and type = #{type,jdbcType=INTEGER}",
            "</if>",
            "<if test='createTime != null and createTime !=\"\"'>",
                "and DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%s') = #{createTime,jdbcType=VARCHAR}",
            "</if>",
            "<if test='departmentIdList != null and departmentIdList.size() > 0'>",
                "and department_id in ",
                "<foreach collection='departmentIdList' index='index' item='departmentId' open='(' separator=',' close=')'>",
                    "#{departmentId,jdbcType=VARCHAR}",
                "</foreach>",
            "</if>",
            "<if test = 'departmentIdList == null or departmentIdList.size == 0'>",
                "and 1 != 1",
            "</if>",
        "</script>"
    })
    @Results({
            @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
            @Result(column="user_id", property="userId", jdbcType=JdbcType.VARCHAR),
            @Result(column="type", property="type", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="amount", property="amount", jdbcType=JdbcType.DECIMAL),
            @Result(column="origin", property="origin", jdbcType=JdbcType.INTEGER),
            @Result(column="pay_time", property="payTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="department_id", property="departmentId", jdbcType=JdbcType.VARCHAR),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="refund_status", property="refundStatus", jdbcType=JdbcType.INTEGER),
            @Result(column="refund_amount", property="refundAmount", jdbcType=JdbcType.DECIMAL),
            @Result(column="refund_time", property="refundTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="refund_mark", property="refundMark", jdbcType=JdbcType.VARCHAR)
    })
    List<Pay> select(PayDto payDto);

    @UpdateProvider(type= PayProvider.class, method="updateById")
    int updateById(Pay pay);

   
}