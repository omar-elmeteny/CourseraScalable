// package com.bugbusters.course.components;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Service;

// import com.bugbusters.course.dto.KafkaMessage;

// @Service
// @lombok.extern.slf4j.Slf4j
// public class KafkaProducerTemplates {

// @Value("${spring.kafka.producer.topic}")
// private String topic;

// private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

// public KafkaProducerTemplates(KafkaTemplate<String, KafkaMessage>
// kafkaTemplate) {
// this.kafkaTemplate = kafkaTemplate;
// }

// public void sendCommand(KafkaMessage command) {
// log.info("#### -> Publishing command -> {}", command);
// kafkaTemplate.send(topic, command);
// }

// }
