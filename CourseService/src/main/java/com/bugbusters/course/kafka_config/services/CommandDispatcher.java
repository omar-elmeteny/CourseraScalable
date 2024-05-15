package com.bugbusters.course.kafka_config.services;

import com.bugbusters.course.kafka_config.exceptions.MessageQueueException;

public interface CommandDispatcher {
    <TRequest> void sendCommand(String commandName, TRequest request) throws MessageQueueException;
}