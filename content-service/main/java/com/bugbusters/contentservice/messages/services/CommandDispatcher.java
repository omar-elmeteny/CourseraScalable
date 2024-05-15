package com.bugbusters.contentservice.messages.services;

import com.bugbusters.contentservice.messages.exceptions.MessageQueueException;

public interface CommandDispatcher {
    <TRequest, TResponse> TResponse sendCommand(String commandName, TRequest request, Class<TResponse> responseType)
            throws MessageQueueException;
}
