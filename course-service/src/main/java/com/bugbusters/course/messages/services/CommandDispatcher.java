package com.bugbusters.course.messages.services;

import com.bugbusters.course.messages.exceptions.MessageQueueException;

public interface CommandDispatcher {
    <TRequest, TResponse> TResponse sendCommand(String commandName, TRequest request, Class<TResponse> responseType)
            throws MessageQueueException;
}
