package com.damai.kafka;

import com.damai.service.ApiDataService;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 damai 来获取项目的完整资料 
 * @description: kafka 消费配置
 * @author: 阿宽不是程序员
 **/
@EnableKafka
@EnableConfigurationProperties(ConsumerProperty.class)
@ConditionalOnProperty(value = "kafka.consumer.servers")
public class ConsumerConfig {
    
    public Map<String, Object> consumerConfigs(ConsumerProperty consumerProperty) {
        Map<String, Object> propsMap = new HashMap<>(16);
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProperty.getServers());
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerProperty.isAutoCommit());
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerProperty.getAutoCommitIntervalMs());
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProperty.getAutoOffsetReset());
        propsMap.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG,consumerProperty.getGroupId());
        return propsMap;
    }
    
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(ConsumerProperty consumerProperty) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs(consumerProperty)));
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(1000);
        return factory;
    }
    
    @Bean
    public ApiDataMessageConsumer apiDataMessageConsumer(ApiDataService apiDataService){
        return new ApiDataMessageConsumer(apiDataService);
    }
}
