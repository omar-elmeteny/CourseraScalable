package com.bugbusters.course.kafka_config.services;

public interface MessageObserver {
    void update(String topic, String key, String message);
}
