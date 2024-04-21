package com.bugbusters.course.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@lombok.extern.slf4j.Slf4j
public class KafKaTopicListeners {

    @KafkaListener(topics = {
            "${spring.kafka.topic}" }, containerFactory = "kafkaListenerStringFactory", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(String message) {
        log.info("**** -> Consumed message -> {}", message);
    }
}