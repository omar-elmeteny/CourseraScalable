package com.bugbusters.contentservice.messages.services;

import com.bugbusters.contentservice.messages.exceptions.MessageQueueException;

public interface MessageProducer {
    void send(String topic, String key, Object message) throws MessageQueueException;
}
