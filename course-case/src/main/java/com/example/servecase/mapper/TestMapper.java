package com.example.servecase.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.servecase.entity.Test;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;


public interface TestMapper extends BaseMapper<Test> {

    @Select({
            "select id, column1, column2, column3, column4, column5, column6, number",
            "from test where id = #{id}"
    })
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.BIGINT),
            @Result(column="column1", property="column1", jdbcType= JdbcType.VARCHAR),
            @Result(column="column2", property="column2", jdbcType= JdbcType.VARCHAR),
            @Result(column="column3", property="column3", jdbcType= JdbcType.VARCHAR),
            @Result(column="column4", property="column4", jdbcType= JdbcType.VARCHAR),
            @Result(column="column5", property="column5", jdbcType= JdbcType.VARCHAR),
            @Result(column="column6", property="column6", jdbcType= JdbcType.VARCHAR),
            @Result(column="number", property="number", jdbcType= JdbcType.BIGINT)
    })
    Test getById(Long id);

//    @Insert({
//            "insert into test (id, column1, column2, column3, column4, column5, column6, number)",
//            "values (#{id,jdbcType=BIGINT}, #{column1,jdbcType=VARCHAR}, ",
//            "#{column2,jdbcType=VARCHAR}, #{column3,jdbcType=VARCHAR}, ",
//            "#{column4,jdbcType=VARCHAR}, #{column5,jdbcType=VARCHAR}, ",
//            "#{column6,jdbcType=VARCHAR}, #{number,jdbcType=BIGINT}",
//            ")"
//    })
//    int insert(Test test);

    @Update({
            "update test set number = #{number,jdbcType=BIGINT} where id = #{id,jdbcType=BIGINT}"
    })
    Integer updateNumberById(@Param("number")Long number, @Param("id")Long id);
}
