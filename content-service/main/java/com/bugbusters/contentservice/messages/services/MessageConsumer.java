package com.bugbusters.contentservice.messages.services;

public interface MessageConsumer {
    Unsubscriber subscribe(String topic, MessageObserver observer);

    void start();
}
