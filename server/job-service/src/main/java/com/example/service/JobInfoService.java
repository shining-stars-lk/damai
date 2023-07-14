package com.example.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.JobInfoDto;
import com.example.dto.JobInfoDtoPage;
import com.example.dto.RunJobDto;
import com.example.entity.JobInfo;
import com.example.mapper.JobInfoMapper;
import com.example.run.ServiceJobRun;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: toolkit
 * @description:
 * @author: kuan
 * @create: 2023-06-28
 **/
@Service
public class JobInfoService extends ServiceImpl<JobInfoMapper, JobInfo> {

    @Autowired
    private JobInfoMapper jobInfoMapper;
    @Resource
    private UidGenerator uidGenerator;
    
    @Autowired
    private ServiceJobRun serviceJobRun;
    
    public Page<JobInfo> pageList(JobInfoDtoPage jobInfoDtoPage){
        Page<JobInfo> page = Page.of(jobInfoDtoPage.getPageNo(), jobInfoDtoPage.getPageSize());
        LambdaQueryWrapper<JobInfo> queryWrapper = new LambdaQueryWrapper<>();
        Page<JobInfo> jobInfoPage = jobInfoMapper.selectPage(page, queryWrapper);
        return jobInfoPage;
    }
    
    public void add(final JobInfoDto jobInfoDto) {
        JobInfo jobInfo = new JobInfo();
        BeanUtils.copyProperties(jobInfoDto,jobInfo);
        jobInfo.setId(String.valueOf(uidGenerator.getUID()));
        jobInfo.setCreateTime(new Date());
        jobInfoMapper.insert(jobInfo);
    }
    
    public Object runJob(final RunJobDto runJobDto) {
        return serviceJobRun.runJob(runJobDto.getId());
    }
}
