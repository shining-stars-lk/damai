package com.damai.test;//package com.damai.service.test;
//
//import com.damai.util.StringUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.stream.MapRecord;
//import org.springframework.data.redis.connection.stream.RecordId;
//import org.springframework.data.redis.core.StreamOperations;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.stream.StreamListener;
//
///**
// * @program: damai
// * @description:
// * @author: k
// * @create: 2024-05-06
// **/
//@Slf4j
//@Configuration
//public class MyStreamMessageListener implements StreamListener<String, MapRecord<String,String, String>> {
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Override
//    public void onMessage(final MapRecord<String, String, String> message) {
//        StreamOperations<String, Object, Object> opsForStream = stringRedisTemplate.opsForStream();
//        RecordId id = message.getId();
//        String value = message.getValue().get("id");
//
//        if (StringUtil.isEmpty(value)){
//            log.error("接收到非法信息：{}",message.getValue());
//            opsForStream.acknowledge(StreamConstant.streamName, StreamConstant.consumerGroup, id);
//            opsForStream.delete(message);
//            return;
//        }
//        // 除去不合法字符
//        value = value.replaceAll("\"","");
//        log.info("received id:{} uuid:{}", id, value);
//        try {
//            // todo 业务
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        } finally {
//            try {
//                final Long acknowledge = opsForStream.acknowledge(
//                        StreamConstant.streamName,
//                        StreamConstant.consumerGroup,
//                        id
//                );
//                Long delete = 0L;
//                if (acknowledge != null && acknowledge == 1L) {
//                    delete = opsForStream.delete(message);
//                }
//                log.info("acknowledge:{} delete:{}",acknowledge, delete);
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }
//}
