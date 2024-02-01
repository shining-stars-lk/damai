package com.example.kafka;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@EnableConfigurationProperties(ProducerProperty.class)
@ConditionalOnProperty(value = "kafka.producer.servers")
public class ProducerConfig {
    
    public Map<String, Object> producerConfigs(ProducerProperty producerProperty) {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperty.getServers());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG, producerProperty.getRetries());
        props.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
    

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerProperty producerProperty) {
        return new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory<>(producerConfigs(producerProperty)));
    }
    
    @Bean
    public ApiDataMessageSend apiDataMessageSend(KafkaTemplate<String, String> kafkaTemplate,ProducerProperty producerProperty){
        return new ApiDataMessageSend(kafkaTemplate,producerProperty.getTopic());
    }
}
