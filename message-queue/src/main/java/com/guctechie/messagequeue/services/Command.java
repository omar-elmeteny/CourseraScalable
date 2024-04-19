package com.guctechie.messagequeue.services;

public interface Command<TRequest, TResponse> {
    TResponse execute(TRequest request) throws Exception;
    Class<TRequest> getRequestType();
    Class<TResponse> getResponseType();
}
