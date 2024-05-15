package com.bugbusters.course.messages.services;

public interface MessageObserver {
    void update(String topic, String key, String message);
}
