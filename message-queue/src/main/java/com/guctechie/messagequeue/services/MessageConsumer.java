package com.guctechie.messagequeue.services;

public interface MessageConsumer {
    Unsubscriber subscribe(String topic, MessageObserver observer);
}
