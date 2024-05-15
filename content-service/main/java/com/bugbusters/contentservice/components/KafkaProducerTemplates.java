// package com.bugbusters.contentservice.components;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.stereotype.Service;
// import com.bugbusters.contentservice.dto.KafkaMessage;

// @Service
// @lombok.extern.slf4j.Slf4j
// public class KafkaProducerTemplates {

// private KafkaTemplate<String, KafkaMessage> CommandKafkaTemplate;

// @Value("${spring.kafka.producer.topic}")
// private String topic;

// public KafkaProducerTemplates(KafkaTemplate<String, KafkaMessage>
// CommandKafkaTemplate) {
// this.CommandKafkaTemplate = CommandKafkaTemplate;
// }

// public void sendCommand(KafkaMessage command) {
// log.info("#### -> Publishing command -> {}", command);
// CommandKafkaTemplate.send(topic, command);
// }

// }
