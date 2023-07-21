package com.example.agent.plugin.thread.wrapper;

import lombok.AllArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

/**
 * @program: cook-frame
 * @description:
 * @author: 星哥
 * @create: 2023-07-21
 **/
@AllArgsConstructor
public class RunnableWrapper extends BaseWrapper implements Runnable{
    
    private final Runnable runnable;
    
    private final Map<String, String> parentMdcContext;
    
    private final Map<String, String> parentHoldContext;
    
    private final RequestAttributes requestAttributes;
    
    @Override
    public void run() {
        Map<String, Map<String, String>> preprocess = preprocess(parentMdcContext, parentHoldContext);
        Map<String, String> holdContext = preprocess.get("holdContext");
        Map<String, String> mdcContext = preprocess.get("mdcContext");
        if (requestAttributes != null) {
            RequestContextHolder.setRequestAttributes(requestAttributes);
        }
        try {
            runnable.run();
        } finally {
            postProcess(mdcContext,holdContext);
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
