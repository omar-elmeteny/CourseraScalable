package com.bugbusters.course.kafka_config.services;

public interface MessageConsumer {
    Unsubscriber subscribe(String topic, MessageObserver observer);

    void start();
}
