package com.guctechie.messagequeue.services;

public interface MessageObserver {
    void update(String topic, String key, String message);
}
