package com.example.run;

import com.alibaba.fastjson.JSONObject;
import com.baidu.fsg.uid.UidGenerator;
import com.example.core.StringUtil;
import com.example.entity.JobInfo;
import com.example.entity.JobRunRecord;
import com.example.enums.BaseCode;
import com.example.enums.JobInfoMethodCode;
import com.example.exception.ToolkitException;
import com.example.service.JobInfoService;
import com.example.service.JobRunRecordService;
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

import static com.example.constant.Constant.JOB_INFO_ID;
import static com.example.constant.Constant.JOB_RUN_RECORD_ID;
import static com.example.constant.Constant.TRACE_ID;

/**
 * @program: toolkit
 * @description:
 * @author: k
 * @create: 2023-06-28
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
    
    public Object runJob(String jobInfoId){
        JobInfo jobInfo = Optional.ofNullable(jobInfoService.getById(jobInfoId)).orElseThrow(() -> new ToolkitException(BaseCode.JOB_INFO_NOT_EXIST));
        String id = jobInfo.getId();
        String url = jobInfo.getUrl();
        String headers = jobInfo.getHeaders();
        Integer method = jobInfo.getMethod();
        String params = jobInfo.getParams();
        
        String traceId = String.valueOf(uidGenerator.getUID());
        
        JSONObject jsonObjectHeaders = new JSONObject();
        if (StringUtil.isNotEmpty(headers)) {
            jsonObjectHeaders = JSONObject.parseObject(headers);
        }
        jsonObjectHeaders.put(JOB_INFO_ID,id);
        jsonObjectHeaders.put(TRACE_ID,traceId);
        
        JobRunRecord jobRunRecord = new JobRunRecord();
        jobRunRecord.setId(String.valueOf(uidGenerator.getUID()));
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
            url = splicingURL(url, jsonObjectParams);
        }
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        
        return Optional.ofNullable(exchange).map(ResponseEntity::getBody).orElse(null);
    }
    
    /**
     * get请求拼接url
     *
     * @param url
     * @param params
     * @return
     */
    private String splicingURL(String url, JSONObject params) {
        if (null == params) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains("?")) {
            if (!url.endsWith("?")) {
                sb.append("&");
            }
        } else {
            sb.append("?");
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
            Map<String, Object> paramMap = new HashMap<>();
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
