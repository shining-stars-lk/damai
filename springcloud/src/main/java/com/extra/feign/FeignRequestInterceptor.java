package com.extra.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: 将请求头的信息传递到下一个微服务链路中
 * @description:
 * @author: lk
 * @create: 2023-04-17
 **/

@Log4j2
public class FeignRequestInterceptor implements RequestInterceptor {
    
    /**
     * 链路id
     * */
    private static final String TRACE_ID = "traceId";
    
    @Override
    public void apply(final RequestTemplate template) {
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (ra != null) {
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                HttpServletRequest request = sra.getRequest();
                String traceId = request.getHeader(TRACE_ID);
                template.header(TRACE_ID,traceId);
            }
        }catch (Exception e) {
            log.error("FeignRequestInterceptor apply error",e);
        }
    }
}
