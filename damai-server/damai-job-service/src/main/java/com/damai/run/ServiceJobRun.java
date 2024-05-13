package com.damai.run;

import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.damai.util.StringUtil;
import com.damai.entity.JobInfo;
import com.damai.entity.JobRunRecord;
import com.damai.enums.BaseCode;
import com.damai.enums.JobInfoMethodCode;
import com.damai.exception.DaMaiFrameException;
import com.damai.service.JobInfoService;
import com.damai.service.JobRunRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.damai.constant.Constant.JOB_INFO_ID;
import static com.damai.constant.Constant.JOB_RUN_RECORD_ID;
import static com.damai.constant.Constant.TRACE_ID;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: job 执行
 * @author: 阿星不是程序员
 **/
@Slf4j
@Component
public class ServiceJobRun {
    
    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    private JobInfoService jobInfoService;
    
    @Autowired
    private JobRunRecordService jobRunRecordService;
    
    @Resource
    private UidGenerator uidGenerator;
    
    public Object runJob(Long jobInfoId){
        JobInfo jobInfo = Optional.ofNullable(jobInfoService.getById(jobInfoId)).orElseThrow(() -> new DaMaiFrameException(BaseCode.JOB_INFO_NOT_EXIST));
        Long id = jobInfo.getId();
        String url = jobInfo.getUrl();
        String headers = jobInfo.getHeaders();
        Integer method = jobInfo.getMethod();
        String params = jobInfo.getParams();
        
        String traceId = String.valueOf(uidGenerator.getUid());
        
        JSONObject jsonObjectHeaders = new JSONObject();
        if (StringUtil.isNotEmpty(headers)) {
            jsonObjectHeaders = JSONObject.parseObject(headers);
        }
        jsonObjectHeaders.put(JOB_INFO_ID,id);
        jsonObjectHeaders.put(TRACE_ID,traceId);
        
        JobRunRecord jobRunRecord = new JobRunRecord();
        jobRunRecord.setId(uidGenerator.getUid());
        jobRunRecord.setJobId(id);
        jobRunRecord.setCreateTime(new Date());
        jobRunRecord.setTraceId(traceId);
        jobRunRecordService.save(jobRunRecord);
        
        jsonObjectHeaders.put(JOB_RUN_RECORD_ID,jobRunRecord.getId());
        
        headers = jsonObjectHeaders.toJSONString();
        
        String result = null;
        try {
            if (Objects.equals(JobInfoMethodCode.GET.getCode(), method)) {
                result = get(url, headers, params);
            } else if (Objects.equals(JobInfoMethodCode.POST.getCode(), method)) {
                result = post(url, headers, params);
            }
            if (StringUtil.isNotEmpty(result)) {
                JSONObject jsonObjectResult = JSONObject.parseObject(result);
                String code = jsonObjectResult.getString("code");
                if (!BaseCode.SUCCESS.getCode().toString().equals(code)) {
                    log.error("job info run result error info : {}",result);
                }
            }
        }catch (Exception e) {
            log.error("job info run error",e);
        }
        return result;
    }
    
    /**
     * get方法调用
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public String get(String url, String headers, String params) {
        HttpHeaders header = new HttpHeaders();
        if (StringUtil.isNotEmpty(headers)) {
            JSONObject jsonObjectHeaders = JSONObject.parseObject(headers);
            for (String headKey : jsonObjectHeaders.keySet()) {
                header.add(headKey, jsonObjectHeaders.getString(headKey));
            }
        }
        HttpEntity<JSONObject> requestEntity = new HttpEntity<JSONObject>(header);
        if (StringUtil.isNotEmpty(params)) {
            JSONObject jsonObjectParams = JSONObject.parseObject(params);
            url = splicingUrl(url, jsonObjectParams);
        }
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        
        return Optional.ofNullable(exchange).map(ResponseEntity::getBody).orElse(null);
    }
    
    /**
     * get请求拼接url
     *
     * @param url 请求
     * @param params 参数
     * @return 结果
     */
    private String splicingUrl(String url, JSONObject params) {
        String questionMark = "?";
        if (null == params) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains(questionMark)) {
            if (!url.endsWith(questionMark)) {
                sb.append("&");
            }
        } else {
            sb.append(questionMark);
        }
        for (String str : params.keySet()) {
            sb.append(str).append("=").append(params.getString(str)).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }
    
    /**
     * post调用
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public String post(String url, String headers, String params) {
        HttpHeaders header = new HttpHeaders();
        boolean isApplication = false;
        if (StringUtil.isNotEmpty(headers)) {
            JSONObject jsonObjectHeaders =  JSONObject.parseObject(headers);
            for (String jsonObjectHeader : jsonObjectHeaders.keySet()) {
                String value = jsonObjectHeaders.getString(jsonObjectHeader);
                if("Content-Type".equals(jsonObjectHeader) && value.contains("application/json")) {
                    isApplication = true;
                }
                header.add(jsonObjectHeader, value);
            }
        }
        String result = null;
        if (isApplication) {
            Map<String, Object> paramMap = new HashMap<>(8);
            if (StringUtil.isNotEmpty(params)) {
                JSONObject jsonObjectParams =  JSONObject.parseObject(params);
                for (String jsonObjectParam : jsonObjectParams.keySet()) {
                    paramMap.put(jsonObjectParam, jsonObjectParams.getString(jsonObjectParam));
                }
            }
            HttpEntity<String> requestEntity = new HttpEntity<>(
                    JSONObject.toJSONString(paramMap), header);
            result = restTemplate.postForObject(url, requestEntity, String.class);
        }else {
            MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
            if (StringUtil.isNotEmpty(params)) {
                JSONObject jsonObjectParams = JSONObject.parseObject(params);
                for (String jsonObjectParam : jsonObjectParams.keySet()) {
                    paramMap.add(jsonObjectParam, jsonObjectParams.getString(jsonObjectParam));
                }
            }
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
                    paramMap, header);
            result = restTemplate.postForObject(url, requestEntity, String.class);
        }
        return result;
    }
    
}
