package com.damai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.damai.entity.JobRunRecord;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
public interface JobRunRecordMapper extends BaseMapper<JobRunRecord> {
    
    int callBack(JobRunRecord jobRunRecord);
}
