package com.example.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static com.example.constant.Constant.CODE;
import static com.example.constant.Constant.TRACE_ID;

/**
 * @program: 将请求头的信息传递到下一个微服务链路中
 * @description:
 * @author: 星哥
 * @create: 2023-04-17
 **/

@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    
    @Override
    public void apply(final RequestTemplate template) {
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (ra != null) {
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                HttpServletRequest request = sra.getRequest();
                String traceId = request.getHeader(TRACE_ID);
                String code = request.getHeader(CODE);
                template.header(TRACE_ID,traceId);
                template.header(CODE,code);
            }
        }catch (Exception e) {
            log.error("FeignRequestInterceptor apply error",e);
        }
    }
}
