package com.bugbusters.course.messages.services;

import com.bugbusters.course.messages.exceptions.MessageQueueException;

public interface MessageSerializer {
    String serialize(Object object) throws MessageQueueException;

    <T> T deserialize(String message, Class<T> type) throws MessageQueueException;
}
