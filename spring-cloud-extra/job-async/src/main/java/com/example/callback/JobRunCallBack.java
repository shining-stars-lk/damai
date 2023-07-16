package com.example.callback;

import com.alibaba.fastjson.JSON;
import com.example.client.JobClient;
import com.example.common.ApiResponse;
import com.example.core.StringUtil;
import com.example.dto.JobCallBackDto;
import com.example.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.constant.Constant.JOB_INFO_ID;
import static com.example.constant.Constant.JOB_RUN_RECORD_ID;

/**
 * @program: toolkit
 * @description:
 * @author: 星哥
 * @create: 2023-06-28
 **/
@Slf4j
@Component
public class JobRunCallBack {

    @Autowired
    private JobClient jobClient;
    
    public void callBack(String runInfo, Integer runStatus){
        String jobInfoId = BaseParameterHolder.getParameter(JOB_INFO_ID);
        String jobRunRecordId = BaseParameterHolder.getParameter(JOB_RUN_RECORD_ID);
        if (StringUtil.isNotEmpty(jobInfoId) && StringUtil.isNotEmpty(jobRunRecordId)) {
            JobCallBackDto jobCallBackDto = new JobCallBackDto();
            jobCallBackDto.setId(jobRunRecordId);
            jobCallBackDto.setJobId(jobInfoId);
            jobCallBackDto.setRunInfo(runInfo);
            jobCallBackDto.setRunStatus(runStatus);
            ApiResponse<Boolean> apiResponse = jobClient.callBack(jobCallBackDto);
            if (apiResponse.getCode() != ApiResponse.ok().getCode().intValue()) {
                log.error("callBack error dto : {} apiResponse : {}", JSON.toJSONString(jobCallBackDto),JSON.toJSONString(apiResponse));
            }
        }
    }
    
}
