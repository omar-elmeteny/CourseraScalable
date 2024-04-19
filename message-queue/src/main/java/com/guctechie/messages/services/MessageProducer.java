package com.guctechie.messages.services;

import com.guctechie.messages.exceptions.MessageQueueException;

public interface MessageProducer {
    void send(String topic, String key, Object message) throws MessageQueueException;
}
