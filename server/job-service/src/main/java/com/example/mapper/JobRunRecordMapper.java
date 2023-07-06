package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.JobRunRecord;
import org.apache.ibatis.annotations.Update;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-28
 **/
public interface JobRunRecordMapper extends BaseMapper<JobRunRecord> {
    
    @Update({
            "update job_run_record",
            "set run_info = #{runInfo,jdbcType=VARCHAR}, run_status = #{runStatus,jdbcType=INTEGER}",
            "where id = #{id,jdbcType=VARCHAR} "
    })
    int callBack(JobRunRecord jobRunRecord);
}
