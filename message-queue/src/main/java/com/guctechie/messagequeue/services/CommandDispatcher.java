package com.guctechie.messagequeue.services;

import com.guctechie.messagequeue.exceptions.MessageQueueException;

public interface CommandDispatcher {
    <TRequest, TResponse> TResponse sendCommand(String commandName, TRequest request, Class<TResponse> responseType) throws MessageQueueException;
}
