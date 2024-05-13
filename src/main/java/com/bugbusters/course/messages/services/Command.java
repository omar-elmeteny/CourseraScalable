package com.bugbusters.course.messages.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Command<TRequest> {
    final Logger logger = LoggerFactory.getLogger(Command.class);

    void execute(TRequest request) throws Exception;

    Class<TRequest> getRequestType();
}
