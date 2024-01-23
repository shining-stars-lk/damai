//package com.example.delay;
//
//import com.luo.delayqueue.DelayTrigger;
//import com.luo.delayqueue.RedissonFastDelayQueueClient;
//
//@RedissonFastDelayQueueClient
//public class OnReceiveMsg {
//    
//    @DelayTrigger
//    public void accept(String body) {
//        System.err.println("======收到延时队列数据 : "+body+"=======");
//    }
//}