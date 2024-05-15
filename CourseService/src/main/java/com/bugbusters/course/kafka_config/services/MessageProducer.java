package com.bugbusters.course.kafka_config.services;

import com.bugbusters.course.kafka_config.exceptions.MessageQueueException;

public interface MessageProducer {
    void send(String topic, String key, Object message) throws MessageQueueException;
}
