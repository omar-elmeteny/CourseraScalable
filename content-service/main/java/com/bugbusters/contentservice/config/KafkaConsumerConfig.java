// package com.bugbusters.contentservice.config;

// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.annotation.EnableKafka;
// import
// org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
// import org.springframework.kafka.listener.KafkaListenerErrorHandler;
// import org.springframework.kafka.support.serializer.JsonDeserializer;
// import com.bugbusters.contentservice.dto.KafkaMessage;
// import java.util.HashMap;
// import java.util.Map;

// @Configuration
// @EnableKafka
// @lombok.extern.slf4j.Slf4j
// public class KafkaConsumerConfig {

// @Value("${spring.kafka.bootstrap-servers}")
// private String bootstrapServers;

// @Value("${spring.kafka.consumer.group-id}")
// private String groupId;

// @Bean
// public ConsumerFactory<String, KafkaMessage> consumerFactory() {
// Map<String, Object> config = new HashMap<>();

// config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
// config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
// config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
// config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
// config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
// StringDeserializer.class);
// config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
// JsonDeserializer.class);
// // config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
// // "com.bugbusters.contentservice.dto.KafkaMessage");
// return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
// new JsonDeserializer<>(KafkaMessage.class));
// }

// @Bean
// public ConcurrentKafkaListenerContainerFactory<String, KafkaMessage>
// kafkaListenerFactory() {
// ConcurrentKafkaListenerContainerFactory<String, KafkaMessage> factory = new
// ConcurrentKafkaListenerContainerFactory<>();
// factory.setConsumerFactory(consumerFactory());
// factory.setBatchListener(true);
// return factory;
// }

// @Bean
// public KafkaListenerErrorHandler myTopicErrorHandler() {
// return (m, e) -> {
// log.error("Got an error {}", e.getMessage());
// return "some info about the failure";
// };
// }

// }
