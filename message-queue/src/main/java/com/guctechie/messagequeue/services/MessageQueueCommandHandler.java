package com.guctechie.messagequeue.services;

import com.guctechie.messagequeue.configs.MessageQueueConfig;
import com.guctechie.messagequeue.exceptions.MessageQueueException;
import com.guctechie.messagequeue.models.CommandRequestMessage;
import com.guctechie.messagequeue.models.CommandResponseMessage;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MessageQueueCommandHandler implements CommandHandler {
    private final HashMap<String, Command<?, ?>> commands = new HashMap<>();
    private final MessageQueueConfig messageQueueConfig;
    private final MessageConsumer messageConsumer;
    private final MessageProducer messageProducer;
    private final MessageSerializer messageSerializer;
    private final Logger logger = LoggerFactory.getLogger(MessageQueueCommandHandler.class);
    private Unsubscriber messageConsumerUnsubscriber;
    private final ExecutorService executor;


    MessageQueueCommandHandler(
            MessageQueueConfig messageQueueConfig,
            MessageConsumer messageConsumer,
            MessageProducer messageProducer,
            MessageSerializer messageSerializer

    ) {
        this.messageQueueConfig = messageQueueConfig;
        this.messageConsumer = messageConsumer;
        this.messageProducer = messageProducer;
        this.messageSerializer = messageSerializer;
        this.executor = Executors.newCachedThreadPool();
        start();
    }


    @Override
    public <TRequest, TResponse> void registerHandler(String commandName, Command<TRequest, TResponse> handler) {
        synchronized (commands) {
            commands.put(commandName, handler);
        }

    }

    @Override
    public void unregisterHandler(String commandName) {
        synchronized (commands) {
            commands.remove(commandName);
        }
    }

    private void start() {
        synchronized (this) {
            if (messageConsumerUnsubscriber != null) {
                return;
            }
            messageConsumerUnsubscriber = messageConsumer.subscribe(messageQueueConfig.getCommandsTopic(), this::handleMessageOnThreadPool);
        }
    }

    @PreDestroy()
    public void stop() {
        synchronized (this) {
            if (messageConsumerUnsubscriber == null) {
                return;
            }
            messageConsumerUnsubscriber.unsubscribe();
            messageConsumerUnsubscriber = null;
        }

    }

    private Command<?,?> getCommand(String commandName) {
        synchronized (commands) {
            return commands.get(commandName);
        }
    }

    private <TRequest, TResponse> Object executeCommand(Command<TRequest, TResponse> command, Object request) throws Exception {
        TRequest req = command.getRequestType().cast(request);
        return command.execute(req);
    }

    private void handleMessageOnThreadPool(String topic, String key, String message) {
        executor.submit(() -> handleMessage(topic, key, message));
    }

    private void handleMessage(String topic, String key, String message) {
        try {
            logger.info("Received message from topic: {} with key: {}", topic, key);
            CommandRequestMessage requestMessage = (CommandRequestMessage)messageSerializer.deserialize(message, CommandRequestMessage.class);
            Command<?,?> command = getCommand(requestMessage.getCommandName());
            if (command == null) {
                throw new MessageQueueException("No handler registered for command: " + requestMessage.getCommandName());
            }

            Object request = messageSerializer.deserialize(requestMessage.getPayload(), command.getRequestType());
            try {
                Object response = executeCommand(command, request);
                CommandResponseMessage responseMessage = new CommandResponseMessage();
                responseMessage.setSuccess(true);
                responseMessage.setKey(key);
                String responseJson = messageSerializer.serialize(response);
                responseMessage.setPayload(responseJson);
                String responseMessageJson = messageSerializer.serialize(responseMessage);
                messageProducer.send(requestMessage.getResponseTopic(), key, responseMessageJson);
                logger.info("Sent response message to topic: {} with key: {}", requestMessage.getResponseTopic(), key);
            } catch (Exception e) {
                logger.error("Error handling message", e);
                CommandResponseMessage responseMessage = new CommandResponseMessage();
                responseMessage.setErrorMessage(e.getMessage());
                responseMessage.setSuccess(false);
                responseMessage.setKey(key);
                String responseMessageJson = messageSerializer.serialize(responseMessage);
                messageProducer.send(requestMessage.getResponseTopic(), key, responseMessageJson);
            }
        } catch (MessageQueueException e) {
            logger.error("Error handling message", e);
        }
    }
}
