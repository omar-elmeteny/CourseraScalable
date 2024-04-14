package com.guctechie.messagequeue.services;

import com.guctechie.messagequeue.exceptions.MessageQueueException;

public interface MessageSerializer {
    String serialize(Object object) throws MessageQueueException;
    <T> T deserialize(String message, Class<T> type) throws MessageQueueException;
}
