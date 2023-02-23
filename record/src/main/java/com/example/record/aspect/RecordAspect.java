package com.example.record.aspect;

import com.alibaba.fastjson.JSON;
import com.example.BusinessThreadPool;
import com.example.core.StringUtil;
import com.example.record.annotion.Record;
import com.example.record.es.RecordEsUtils;
import com.example.record.provider.RecordBaseInfoProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: lk
 * @create: 2023-02-20
 **/
@Aspect
@Order(-8)
public class RecordAspect extends RecordBaseInfoProvider {
    
    private final Logger logger = LoggerFactory.getLogger(RecordAspect.class);
    
    private RecordEsUtils recordEsUtils;
    
    public RecordAspect(RecordEsUtils recordEsUtils){
        this.recordEsUtils = recordEsUtils;
    }
    
    @Around("@annotation(record)")
    public Object around(ProceedingJoinPoint joinPoint, Record record) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String operatorFlag = request.getHeader(OPERATOR_FLAG);
        if (StringUtil.isNotEmpty(operatorFlag) && OPERATOR_FLAG_YES.equals(operatorFlag))  {
            Map<String,Object> parameterEsMap = new HashMap<>();
            try {
                addParameter(request,parameterEsMap,joinPoint,record);
            }catch (Exception e) {
                logger.error("record日志记录生成错误!",e);
            }
            Object proceed = joinPoint.proceed();
            try {
                BusinessThreadPool.execute(() -> {
                    try {
                        //返回结果
                        String result = JSON.toJSONString(proceed);
                        parameterEsMap.put("result",result);
                        recordEsUtils.add(parameterEsMap);
                    }catch (Exception e) {
                        logger.error("record日志输出到es错误",e);
                    }
                });
            }catch (Exception e) {
                logger.error("线程池执行record日志输出任务失败",e);
            }
            return proceed;
        }else {
            return joinPoint.proceed();
        }
    }
    
    public void addParameter(HttpServletRequest request, Map<String,Object> parameterEsMap, ProceedingJoinPoint joinPoint, Record record) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ServletInputStream sis = request.getInputStream();
        String requestBodyStr  = StringUtil.inputStream2String(sis);
        Map<String, String> requestBodyContent = JSON.parseObject(requestBodyStr, Map.class);
        //操作名称
        String operatorName = record.operatorName();
        parameterEsMap.put("operator_name",operatorName);
        //contentType
        String contentType = request.getHeader("Content-Type");
        parameterEsMap.put("content_type",contentType);
        //请求体
        String requestBody = JSON.toJSONString(requestBodyContent);
        parameterEsMap.put("request_body",requestBody);
        //请求方式
        String method = request.getMethod();
        parameterEsMap.put("method",method);
        //请求参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        String parameter = JSON.toJSONString(parameterMap);
        parameterEsMap.put("parameter",parameter);
        //请求地址
        String requestURI = request.getRequestURI();
        parameterEsMap.put("request_uri",requestURI);
        //ip地址
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        parameterEsMap.put("ip_address",ipAddress);
        //时间戳
        parameterEsMap.put("@timestamp",new Date());
        //日志内容
        String definitionContent = getDefinitionContent(joinPoint, record.content());
        String content = executeFunction(definitionContent, joinPoint);
        parameterEsMap.put("content",content);
    }
}
