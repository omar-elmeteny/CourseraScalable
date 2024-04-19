package com.guctechie.messages.services;

import com.guctechie.messages.configs.MessageQueueConfig;
import com.guctechie.messages.configs.WebServerConfig;
import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.models.CommandRequestMessage;
import com.guctechie.messages.models.CommandResponseMessage;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class MessageQueueCommandDispatcher implements CommandDispatcher {

    private final MessageConsumer messageConsumer;
    private final MessageProducer messageProducer;
    private final MessageSerializer messageSerializer;
    private final MessageQueueConfig messageQueueConfig;
    private final WebServerConfig webServerConfig;
    private final Logger logger = LoggerFactory.getLogger(MessageQueueCommandDispatcher.class);
    private Unsubscriber messageConsumerUnsubscriber;
    private final HashMap<String, CompletableFuture<CommandResponseMessage>> tasks = new HashMap<>();

    public MessageQueueCommandDispatcher(
            MessageConsumer messageConsumer,
            MessageProducer messageProducer,
            MessageSerializer messageSerializer,
            MessageQueueConfig messageQueueConfig,
            WebServerConfig webServerConfig
    ) {

        this.messageConsumer = messageConsumer;
        this.messageProducer = messageProducer;
        this.messageSerializer = messageSerializer;
        this.messageQueueConfig = messageQueueConfig;
        this.webServerConfig = webServerConfig;

        this.start();
    }

    @Override
    public <TRequest, TResponse> TResponse sendCommand(String commandName, TRequest request, Class<TResponse> responseType) throws MessageQueueException {
        CommandRequestMessage commandRequestMessage = new CommandRequestMessage();
        commandRequestMessage.setCommandName(commandName);
        commandRequestMessage.setPayload(messageSerializer.serialize(request));
        commandRequestMessage.setResponseTopic(webServerConfig.getResponseTopic());

        UUID key = UUID.randomUUID();

        CompletableFuture<CommandResponseMessage> future = new CompletableFuture<>();
        synchronized (tasks) {
            tasks.put(key.toString(), future);
        }
        messageProducer.send(messageQueueConfig.getCommandsTopic(), key.toString(), commandRequestMessage);

        try {
            CommandResponseMessage responseMessage = future.get();
            if (!responseMessage.isSuccess()) {
                throw new MessageQueueException(responseMessage.getErrorMessage());
            }
            return messageSerializer.deserialize(responseMessage.getPayload(), responseType);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void start() {
        synchronized (this) {
            if (messageConsumerUnsubscriber != null) {
                return;
            }
            messageConsumerUnsubscriber = messageConsumer.subscribe(webServerConfig.getResponseTopic(), this::handleResponseMessage);
        }
    }

    @PreDestroy
    public void stop() {
        synchronized (this) {
            if (messageConsumerUnsubscriber == null) {
                return;
            }
            messageConsumerUnsubscriber.unsubscribe();
            messageConsumerUnsubscriber = null;
        }
    }

    private void handleResponseMessage(String topic, String key, String message) {
        CommandResponseMessage responseMessage;
        try {
            responseMessage = messageSerializer.deserialize(message, CommandResponseMessage.class);
        } catch (MessageQueueException e) {
            logger.error("Error deserializing response message", e);
            return;
        }
        CompletableFuture<CommandResponseMessage> future;
        synchronized (tasks) {
            future = tasks.remove(key);
        }
        if (future == null) {
            logger.warn("No task found for key: {}", key);
            return;
        }

        future.complete(responseMessage);
    }
}
