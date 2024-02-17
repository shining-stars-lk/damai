package com.damai.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.dto.JobCallBackDto;
import com.damai.entity.JobRunRecord;
import com.damai.mapper.JobRunRecordMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Service
public class JobRunRecordService extends ServiceImpl<JobRunRecordMapper, JobRunRecord> {
    
    @Autowired
    private JobRunRecordMapper jobRunRecordMapper;
    
    public int callBack(JobCallBackDto jobCallBackDto) {
        JobRunRecord jobRunRecord = new JobRunRecord();
        BeanUtils.copyProperties(jobCallBackDto,jobRunRecord);
        return jobRunRecordMapper.callBack(jobRunRecord);
    }
}
