package com.damai.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿宽不是程序员 微信，添加时备注 大麦 来获取项目的完整资料 
 * @description: kafka 生产者配置
 * @author: 阿宽不是程序员
 **/
@EnableKafka
@EnableConfigurationProperties(ProducerProperty.class)
@ConditionalOnProperty(value = "kafka.producer.servers")
public class ProducerConfig {
    
    public Map<String, Object> producerConfigs(ProducerProperty producerProperty) {
        Map<String, Object> props = new HashMap<>(16);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperty.getServers());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, producerProperty.getRetries());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
    

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerProperty producerProperty) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs(producerProperty)));
    }
    
    @Bean
    public ApiDataMessageSend apiDataMessageSend(KafkaTemplate<String, String> kafkaTemplate,ProducerProperty producerProperty){
        return new ApiDataMessageSend(kafkaTemplate,producerProperty.getTopic());
    }
}
