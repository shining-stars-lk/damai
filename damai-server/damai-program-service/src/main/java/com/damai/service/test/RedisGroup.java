//package com.damai.service.test;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.connection.stream.RecordId;
//import org.springframework.data.redis.connection.stream.StreamInfo;
//import org.springframework.data.redis.connection.stream.StreamInfo.XInfoGroup;
//import org.springframework.data.redis.connection.stream.StreamRecords;
//import org.springframework.data.redis.core.StreamOperations;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * @program: damai
// * @description:
// * @author: k
// * @create: 2024-05-06
// **/
//@Slf4j
//@Component
//public class RedisGroup {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    private final AtomicBoolean isCreated = new AtomicBoolean(false);
//
//    @PostConstruct
//    public void groupInfo() {
//
//        // 发送个心跳,保证stream已经存在
//        HashMap<Object, Object> map = new HashMap<>();
//        map.put("fileBeat","fileBeat...");
//        final MapRecord<String, Object, Object> record = StreamRecords.newRecord()
//                .in(StreamConstant.streamName)
//                .ofMap(map)
//                .withId(RecordId.autoGenerate());
//
//        final StreamOperations<String, Object, Object> stream = stringRedisTemplate.opsForStream();
//        stream.add(record);
//
//        final StreamInfo.XInfoGroups xInfoGroups = stream.groups(StreamConstant.streamName);
//
//        Collection<XInfoGroup> needDestroyColl = new ArrayList<>();
//
//        xInfoGroups.forEach(xInfoStream -> {
//            if (xInfoStream.groupName().equals(StreamConstant.consumerGroup)) {
//                isCreated.set(true);
//            } else {
//                needDestroyColl.add(xInfoStream);
//            }
//        });
//
//        for (StreamInfo.XInfoGroup xInfoGroup : needDestroyColl) {
//            log.info("destroy consumer group[{}]...", xInfoGroup.groupName());
//            stream.destroyGroup(StreamConstant.streamName,xInfoGroup.groupName());
//        }
//
//        if (isCreated.get()) {
//            return;
//        }
//
//        log.info("create consumer group[{}]...", StreamConstant.consumerGroup);
//
//        stream.createGroup(StreamConstant.streamName, StreamConstant.consumerGroup);
//
//    }
//}
