package com.damai.callback;

import com.alibaba.fastjson.JSON;
import com.damai.client.JobClient;
import com.damai.common.ApiResponse;
import com.damai.util.StringUtil;
import com.damai.dto.JobCallBackDto;
import com.damai.threadlocal.BaseParameterHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.damai.constant.Constant.JOB_INFO_ID;
import static com.damai.constant.Constant.JOB_RUN_RECORD_ID;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: 任务异步执行上报
 * @author: 阿宽不是程序员
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
            jobCallBackDto.setId(Long.parseLong(jobRunRecordId));
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
