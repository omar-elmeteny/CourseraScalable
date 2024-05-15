package com.guctechie.messages.services;

import com.guctechie.messages.exceptions.MessageQueueException;

public interface MessageSerializer {
    String serialize(Object object) throws MessageQueueException;
    <T> T deserialize(String message, Class<T> type) throws MessageQueueException;
}
