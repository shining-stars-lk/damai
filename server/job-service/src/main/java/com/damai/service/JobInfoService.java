package com.damai.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.damai.dto.JobInfoDto;
import com.damai.dto.JobInfoDtoPage;
import com.damai.dto.RunJobDto;
import com.damai.entity.JobInfo;
import com.damai.mapper.JobInfoMapper;
import com.damai.run.ServiceJobRun;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
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
        jobInfo.setId(uidGenerator.getUID());
        jobInfo.setCreateTime(new Date());
        jobInfoMapper.insert(jobInfo);
    }
    
    public Object runJob(final RunJobDto runJobDto) {
        return serviceJobRun.runJob(runJobDto.getId());
    }
}
