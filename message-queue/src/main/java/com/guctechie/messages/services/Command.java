package com.guctechie.messages.services;

public interface Command<TRequest, TResponse> {
    TResponse execute(TRequest request) throws Exception;
    Class<TRequest> getRequestType();
}
