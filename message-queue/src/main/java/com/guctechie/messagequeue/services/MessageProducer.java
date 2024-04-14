package com.guctechie.messagequeue.services;

import com.guctechie.messagequeue.exceptions.MessageQueueException;

public interface MessageProducer {
    void send(String topic, String key, Object message) throws MessageQueueException;
}
