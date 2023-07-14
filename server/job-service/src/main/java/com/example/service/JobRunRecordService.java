package com.example.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.JobCallBackDto;
import com.example.entity.JobRunRecord;
import com.example.mapper.JobRunRecordMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
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
