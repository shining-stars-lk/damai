package com.example.record.aspect;

import com.alibaba.fastjson.JSON;
import com.example.BusinessThreadPool;
import com.example.core.StringUtil;
import com.example.record.annotion.Record;
import com.example.record.es.RecordEsUtils;
import com.example.record.function.ParseFunctionFactory;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @program: record
 * @description:
 * @author: lk
 * @create: 2023-02-20
 **/
@Aspect
@Order(-8)
public class RecordAspect extends RecordBaseInfoProvider {
    
    private final Logger logger = LoggerFactory.getLogger(RecordAspect.class);
    
    public static final String FAIL_CONTENT = "fail_content";
    
    public static final String PROCEED = "proceed";
    
    public static final String ERROR_MESSAGE = "error_message";
    
    private RecordEsUtils recordEsUtils;
    
    public RecordAspect(ParseFunctionFactory parseFunctionFactory, RecordEsUtils recordEsUtils){
        super(parseFunctionFactory);
        this.recordEsUtils = recordEsUtils;
    }
    
    @Around("@annotation(record)")
    public Object around(ProceedingJoinPoint joinPoint, Record record) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String operatorFlag = request.getHeader(OPERATOR_FLAG);
        logger.info("record around operatorFlag:{}",operatorFlag);
        if (StringUtil.isNotEmpty(operatorFlag) && OPERATOR_FLAG_YES.equals(operatorFlag))  {
            Map<String,Object> parameterEsMap = new HashMap<>(64);
            try {
                addParameter(request,parameterEsMap,joinPoint,record);
            }catch (Throwable e) {
                logger.error("record日志记录生成错误!",e);
            }
            long startTimeMillis = System.currentTimeMillis();
            Map<String,Object> executeMap = new HashMap<>(2);
            Object proceed = null;
            String errorMessage = null;
            try{
                proceed = joinPoint.proceed();
                return proceed;
            }catch (Throwable e) {
                throw new Exception(e);
            }finally {
                executeMap.put(PROCEED,proceed);
                executeMap.put(ERROR_MESSAGE,errorMessage);
                parameterEsMap.put("execution_time",System.currentTimeMillis() - startTimeMillis);
                parameterEsMap.put("end_time", System.currentTimeMillis());
                try {
                    BusinessThreadPool.execute(() -> {
                        try {
                            //返回结果
                            if (executeMap.get(PROCEED) != null) {
                                String result = JSON.toJSONString(executeMap.get(PROCEED));
                                parameterEsMap.put("result",result);
                            }
                            //异常错误信息
                            if (executeMap.get(ERROR_MESSAGE) != null) {
                                parameterEsMap.put(ERROR_MESSAGE,executeMap.get(ERROR_MESSAGE));
                            }
                            //执行content中executeBefore=false的apply函数
                            Object content = parameterEsMap.get("content");
                            if (content != null) {
                                String contentNewest = executeFunction((String) content, joinPoint, false);
                                parameterEsMap.put("content",contentNewest);
                            }
                            //处理failContent
                            dealFailContent(joinPoint, executeMap, parameterEsMap);
                            recordEsUtils.add(parameterEsMap);
                        }catch (Throwable e) {
                            logger.error("record日志输出到es错误",e);
                        }
                    });
                }catch (Throwable e) {
                    logger.error("线程池执行record日志输出任务失败",e);
                }
            }
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
        if (StringUtil.isNotEmpty(contentType) && contentType.contains("multipart/form-data")) {
            parameterEsMap.put("request_body","this is a file");
        }else {
            String requestBody = JSON.toJSONString(requestBodyContent);
            parameterEsMap.put("request_body",requestBody);
        }
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
        //版本号
        String header = request.getHeader("api-version");
        parameterEsMap.put( "api_version",header);
        //ip地址
        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        parameterEsMap.put("ip_address",ipAddress);
        //时间戳
        parameterEsMap.put("@timestamp",System.currentTimeMillis());
        //日志内容
        String definitionContent = getDefinitionContent(joinPoint, record.content());
        //执行content中executeBefore=true的apply函数
        String content = executeFunction(definitionContent, joinPoint, true);
        parameterEsMap.put("content",content);
        //执行failContent中executeBefore=true的apply函数
        if (StringUtil.isNotEmpty(record.failContent())) {
            parameterEsMap.put(FAIL_CONTENT,executeFunction(record.failContent(), joinPoint, true));
        }
        
    }
    
    /**
     * 处理failContent
     * */
    public void dealFailContent(ProceedingJoinPoint joinPoint, Map<String,Object> executeMap, Map<String,Object> parameterEsMap) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Object failContent = parameterEsMap.remove(FAIL_CONTENT);
        if (failContent != null) {
            //执行failContent中executeBefore=false的apply函数
            String failContentNewest = executeFunction((String) failContent, joinPoint, false);
            //异常信息进行填充
            if (parameterEsMap.get(FAIL_CONTENT) == null && parameterEsMap.get(ERROR_MESSAGE) != null) {
                failContentNewest = getDefinitionContent(joinPoint,failContentNewest,(String)parameterEsMap.get(ERROR_MESSAGE));
                parameterEsMap.put(FAIL_CONTENT,failContentNewest);
            }
            if (parameterEsMap.get(FAIL_CONTENT) != null) {
                parameterEsMap.remove("content");
            }
        }
    }
}
