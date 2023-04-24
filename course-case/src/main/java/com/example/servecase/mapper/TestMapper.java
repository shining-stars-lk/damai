package com.example.servecase.mapper;


import com.example.servecase.entity.Test;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;


public interface TestMapper {

    @Select({
            "select id, column_1, column_2, column_3, column_4, column_5, column_6, number",
            "from test where id = #{id}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT),
            @Result(column="column_1", property="column1", jdbcType= JdbcType.VARCHAR),
            @Result(column="column_2", property="column2", jdbcType= JdbcType.VARCHAR),
            @Result(column="column_3", property="column3", jdbcType= JdbcType.VARCHAR),
            @Result(column="column_4", property="column4", jdbcType= JdbcType.VARCHAR),
            @Result(column="column_5", property="column5", jdbcType= JdbcType.VARCHAR),
            @Result(column="column_6", property="column6", jdbcType= JdbcType.VARCHAR),
            @Result(column="number", property="number", jdbcType= JdbcType.BIGINT)
    })
    Test getById(Long id);

    @Insert({
            "insert into test (id, column_1, column_2, column_3, column_4, column_5, column_6, number)",
            "values (#{id,jdbcType=BIGINT}, #{column1,jdbcType=VARCHAR}, ",
            "#{column2,jdbcType=VARCHAR}, #{column3,jdbcType=VARCHAR}, ",
            "#{column4,jdbcType=VARCHAR}, #{column5,jdbcType=VARCHAR}, ",
            "#{column6,jdbcType=VARCHAR}, #{number,jdbcType=BIGINT}",
            ")"
    })
    int insert(Test test);

    @Update({
            "update test set number = #{number,jdbcType=BIGINT} where id = #{id,jdbcType=BIGINT}"
    })
    Integer updateNumberById(@Param("number")Long number, @Param("id")Long id);
}
