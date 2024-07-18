package com.damai.feign;

import com.damai.util.StringUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;

import static com.damai.constant.Constant.CODE;
import static com.damai.constant.Constant.GRAY_PARAMETER;
import static com.damai.constant.Constant.TRACE_ID;


/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: feign 参数传递
 * @author: 阿星不是程序员
 **/

@Slf4j
@AllArgsConstructor
public class FeignRequestInterceptor implements RequestInterceptor {
    
    private final String serverGray;
    
    @Override
    public void apply(RequestTemplate template) {
        try {
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (Objects.nonNull(ra)) {
                ServletRequestAttributes sra = (ServletRequestAttributes) ra;
                HttpServletRequest request = sra.getRequest();
                String traceId = request.getHeader(TRACE_ID);
                String code = request.getHeader(CODE);
                String gray = request.getHeader(GRAY_PARAMETER);
                if (StringUtil.isEmpty(gray)) {
                    gray = serverGray;
                }
                template.header(TRACE_ID,traceId);
                template.header(CODE,code);
                template.header(GRAY_PARAMETER,gray);
            }
        }catch (Exception e) {
            log.error("FeignRequestInterceptor apply error",e);
        }
    }
}
