package com.bugbusters.contentservice.messages.services;

public interface MessageObserver {
    void update(String topic, String key, String message);
}
