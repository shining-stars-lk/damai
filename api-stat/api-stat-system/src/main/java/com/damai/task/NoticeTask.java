package com.damai.task;

import com.damai.notice.ApiStatNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.damai.constant.ApiStatConstant.PLATFORM_NOTICE;

/**
 * @program: cook-frame
 * @description:
 * @author: k
 * @create: 2023-10-12
 **/
@Component
public class NoticeTask {
    
    @Autowired
    private ApiStatNotice apiStatNotice;
    
    @Scheduled(cron ="0 0 9 * * ? ")
    public void scheduleNotice() {
        apiStatNotice.notice(PLATFORM_NOTICE);
    }
}
