package com.guctechie.messagequeue.services;

public interface CommandHandler {
    <TRequest, TResponse> void registerHandler(String commandName, Command<TRequest, TResponse> handler);
    void unregisterHandler(String commandName);
    void start();
}
