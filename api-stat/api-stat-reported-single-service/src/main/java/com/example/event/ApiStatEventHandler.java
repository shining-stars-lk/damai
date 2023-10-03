package com.example.event;

import com.example.operate.MethodQueueOperate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;

@AllArgsConstructor
@Slf4j
public class ApiStatEventHandler implements ApplicationListener<ApiStatEvent> {

    private final MethodQueueOperate methodQueueOperate;


    @Override
    public void onApplicationEvent(ApiStatEvent event) {
        new Thread(() -> {
            try {
                log.info("take method queue...");
                methodQueueOperate.takeTask();
            }catch (Exception e) {
                log.error("take method queue error",e);
            }
        }).start();
    }

}
