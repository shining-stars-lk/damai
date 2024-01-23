package com.example.delay;

import com.example.delayqueuenew.context.DelayQueueContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class OrderController {
    
    @Autowired
    private DelayQueueContext delayQueueContext;
    
    @GetMapping("/set")
    public void set() {
        //FastDelayQueueContext.send(OnReceiveMsg.class, "我是发送的数据", 2);
        delayQueueContext.sendMessage("test","测试数据",2, TimeUnit.SECONDS);
    }
}