package com.bugbusters.course.kafka_config.exceptions;

public class MessageQueueException extends Exception {

    public MessageQueueException(String message) {
        super(message);
    }

    public MessageQueueException(String message, Throwable cause) {
        super(message, cause);
    }
}
