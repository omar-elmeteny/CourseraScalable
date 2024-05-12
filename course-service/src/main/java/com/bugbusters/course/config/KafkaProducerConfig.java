// package com.bugbusters.course.config;

// import java.util.HashMap;
// import java.util.Map;

// import org.apache.kafka.clients.producer.ProducerConfig;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Primary;
// import org.springframework.kafka.core.DefaultKafkaProducerFactory;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.core.ProducerFactory;
// import org.springframework.kafka.support.serializer.JsonSerializer;
// import com.bugbusters.course.dto.KafkaMessage;
// import org.apache.kafka.common.serialization.StringSerializer;

// @Configuration
// public class KafkaProducerConfig {

// @Value("${spring.kafka.bootstrap-servers}")
// private String bootstrapServers;

// @Bean
// public ProducerFactory<String, KafkaMessage> producerFactory() {
// Map<String, Object> configProps = new HashMap<>();

// configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
// configProps.put(ProducerConfig.ACKS_CONFIG, "all");
// configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
// configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
// StringSerializer.class);
// configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
// JsonSerializer.class);
// configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
// return new DefaultKafkaProducerFactory<>(configProps);
// }

// @Primary
// @Bean
// public KafkaTemplate<String, KafkaMessage> kafkaTemplate() {
// return new KafkaTemplate<>(producerFactory());
// }

// }
