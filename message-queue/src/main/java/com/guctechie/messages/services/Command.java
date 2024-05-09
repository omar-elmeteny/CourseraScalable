package com.guctechie.messages.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Command<TRequest, TResponse> {
    final Logger logger = LoggerFactory.getLogger(Command.class);
    TResponse execute(TRequest request) throws Exception;
    Class<TRequest> getRequestType();
}
