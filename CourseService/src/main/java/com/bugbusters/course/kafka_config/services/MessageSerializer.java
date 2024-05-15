package com.bugbusters.course.kafka_config.services;

import com.bugbusters.course.kafka_config.exceptions.MessageQueueException;

public interface MessageSerializer {
    String serialize(Object object) throws MessageQueueException;

    <T> T deserialize(String message, Class<T> type) throws MessageQueueException;
}
