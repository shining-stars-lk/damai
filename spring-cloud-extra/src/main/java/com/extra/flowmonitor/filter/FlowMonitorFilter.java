package com.extra.flowmonitor.filter;

import com.extra.flowmonitor.common.FlowMonitorFrameTypeEnum;
import com.extra.flowmonitor.context.FlowMonitorEntity;
import com.extra.flowmonitor.context.FlowMonitorRuntimeContext;
import com.extra.flowmonitor.context.FlowMonitorVirtualUriLoader;
import com.extra.flowmonitor.enhancer.SpringMvcInterceptor;
import com.extra.flowmonitor.provider.FlowMonitorSourceParamProviderFactory;
import com.extra.flowmonitor.toolkit.SystemClock;
import lombok.SneakyThrows;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: 
 * @description:
 * @author: lk
 * @create: 2023-04-10
 **/
public class FlowMonitorFilter extends OncePerRequestFilter {
    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            try {
                if (!FlowMonitorRuntimeContext.hasFilterPath(httpServletRequest.getRequestURI())) {
                    FlowMonitorRuntimeContext.pushEnhancerType(FlowMonitorFrameTypeEnum.SPRING_MVC);
                    FlowMonitorVirtualUriLoader.loadProviderUris();
                    SpringMvcInterceptor.loadResource(httpServletRequest);
                    FlowMonitorRuntimeContext.setExecuteTime();
                }
            }catch (Exception e) {
                logger.error("FlowMonitorFilter flow monitor report error",e);
            }
            boolean existsException = false;
            try{
                filterChain.doFilter(request, response);
            }catch (Throwable e) {
                existsException = true;
                throw new Exception(e);
            }finally {
                try {
                    FlowMonitorEntity sourceParam = FlowMonitorSourceParamProviderFactory.getInstance(httpServletRequest);
                    FlowMonitorEntity flowMonitorEntity = FlowMonitorRuntimeContext.getHost(sourceParam.getTargetResource(), sourceParam.getSourceApplication(), sourceParam.getSourceIpPort());
                    if (existsException) {
                        flowMonitorEntity.getFlowHelper().incrException();
                    } else {
                        flowMonitorEntity.getFlowHelper().incrSuccess(SystemClock.now() - FlowMonitorRuntimeContext.getExecuteTime());
                    }
                }catch (Throwable e) {
                    logger.error("FlowMonitorFilter flow monitor incr error",e);
                }
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }
}
