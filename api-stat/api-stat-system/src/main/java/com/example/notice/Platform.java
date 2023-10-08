package com.example.notice;

import com.alibaba.fastjson.JSON;
import com.example.notice.config.ApiStatNoticeProperties;
import com.example.structure.MethodNoticeData;
import lombok.AllArgsConstructor;

import java.util.List;

/**
 * @program: cook-frame
 * @description:
 * @author: lk
 * @create: 2023-10-07
 **/
@AllArgsConstructor
public abstract class Platform {
    
    protected final ApiStatNoticeProperties apiStatNoticeProperties;
    
    private final String REL_MESSAGE_TEMPLATE = "获得执行时间耗时在%s和%s之间，从第%s开始获取%s的记录为:%s";
    
    public void sendMessage(List<MethodNoticeData> methodNoticeDataList) {
        String relMessage = String.format(REL_MESSAGE_TEMPLATE, apiStatNoticeProperties.getMin(), apiStatNoticeProperties.getMax(), 
                apiStatNoticeProperties.getStart(), apiStatNoticeProperties.getEnd(), JSON.toJSONString(methodNoticeDataList));
        sendRelMessage(relMessage);
    }
    public abstract void sendRelMessage(String content);
}
