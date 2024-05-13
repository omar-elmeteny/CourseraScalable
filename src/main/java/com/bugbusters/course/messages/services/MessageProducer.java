package com.bugbusters.course.messages.services;

import com.bugbusters.course.messages.exceptions.MessageQueueException;

public interface MessageProducer {
    void send(String topic, String key, Object message) throws MessageQueueException;
}
