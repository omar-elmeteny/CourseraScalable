// package com.bugbusters.course.config;

// import java.util.HashMap;
// import java.util.Map;

// import org.apache.kafka.clients.admin.AdminClientConfig;
// import org.apache.kafka.clients.admin.NewTopic;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.core.KafkaAdmin;

// import lombok.extern.slf4j.Slf4j;

// @Configuration
// @Slf4j
// public class KafkaTopicConfig {
    
//     @Value(value = "${spring.kafka.bootstrap-servers}")
//     private String bootstrapAddress;

//     @Bean
//     public KafkaAdmin kafkaAdmin() {
//         log.info("KafkaAdmin bean created");
//         log.info("bootstrapAddress: " + bootstrapAddress);
//         Map<String, Object> configs = new HashMap<>();
//         configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
//         return new KafkaAdmin(configs);
//     }

//     @Bean
//     public NewTopic topic1() {
//         return new NewTopic("content-updates", 1, (short) 1);
//     }
// }
