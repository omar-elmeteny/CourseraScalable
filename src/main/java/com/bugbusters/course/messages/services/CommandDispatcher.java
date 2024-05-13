package com.bugbusters.course.messages.services;

import com.bugbusters.course.messages.exceptions.MessageQueueException;

public interface CommandDispatcher {
    <TRequest> void sendCommand(String commandName, TRequest request) throws MessageQueueException;
}