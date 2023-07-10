package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.JobRunRecord;

/**
 * @program: toolkit
 * @description:
 * @author: lk
 * @create: 2023-06-28
 **/
public interface JobRunRecordMapper extends BaseMapper<JobRunRecord> {
    
    int callBack(JobRunRecord jobRunRecord);
}
