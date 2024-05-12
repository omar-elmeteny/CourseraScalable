package com.bugbusters.contentservice.messages.services;

import com.bugbusters.contentservice.messages.exceptions.MessageQueueException;

public interface MessageSerializer {
    String serialize(Object object) throws MessageQueueException;

    <T> T deserialize(String message, Class<T> type) throws MessageQueueException;
}
