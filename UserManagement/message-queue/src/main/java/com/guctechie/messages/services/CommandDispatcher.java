package com.guctechie.messages.services;

import com.guctechie.messages.exceptions.MessageQueueException;

public interface CommandDispatcher {
    <TRequest, TResponse> TResponse sendCommand(String commandName, TRequest request, Class<TResponse> responseType) throws MessageQueueException;
}
