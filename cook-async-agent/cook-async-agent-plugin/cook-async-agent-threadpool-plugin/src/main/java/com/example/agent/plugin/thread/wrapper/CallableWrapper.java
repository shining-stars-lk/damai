package com.example.agent.plugin.thread.wrapper;

import lombok.AllArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-07-21
 **/
@AllArgsConstructor
public class CallableWrapper extends BaseWrapper implements Callable {
    
    private final Callable callable;
    
    private final Map<String, String> parentMdcContext;
    
    private final Map<String, String> parentHoldContext;
    
    private final RequestAttributes requestAttributes;
    
    @Override
    public Object call() throws Exception {
        Map<String, Map<String, String>> preprocess = preprocess(parentMdcContext, parentHoldContext);
        Map<String, String> holdContext = preprocess.get("holdContext");
        Map<String, String> mdcContext = preprocess.get("mdcContext");
        if (requestAttributes != null) {
            RequestContextHolder.setRequestAttributes(requestAttributes);
        }
        try {
            return callable.call();
        }finally {
            postProcess(mdcContext,holdContext);
            RequestContextHolder.resetRequestAttributes();
        }
    }
}
