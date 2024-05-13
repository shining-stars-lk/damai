package com.damai.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.dto.JobCallBackDto;
import com.damai.entity.JobRunRecord;
import com.damai.mapper.JobRunRecordMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job记录 service
 * @author: 阿星不是程序员
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
