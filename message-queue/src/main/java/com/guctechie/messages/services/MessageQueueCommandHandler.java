package com.guctechie.messages.services;

import com.guctechie.messages.configs.MessageQueueConfig;
import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.models.CommandRequestMessage;
import com.guctechie.messages.models.CommandResponseMessage;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MessageQueueCommandHandler implements CommandHandler {
    private final MessageQueueConfig messageQueueConfig;
    private final MessageConsumer messageConsumer;
    private final MessageProducer messageProducer;
    private final MessageSerializer messageSerializer;
    private final ApplicationContext applicationContext;
    private final Logger logger = LoggerFactory.getLogger(MessageQueueCommandHandler.class);
    private Unsubscriber messageConsumerUnsubscriber;
    private final ExecutorService executor;

    public MessageQueueCommandHandler(
            MessageQueueConfig messageQueueConfig,
            MessageConsumer messageConsumer,
            MessageProducer messageProducer,
            MessageSerializer messageSerializer,
            ApplicationContext applicationContext
    ) {
        this.messageQueueConfig = messageQueueConfig;
        this.messageConsumer = messageConsumer;
        this.messageProducer = messageProducer;
        this.messageSerializer = messageSerializer;
        this.applicationContext = applicationContext;
        this.executor = Executors.newCachedThreadPool();
    }


//    @Override
//    public <TRequest, TResponse> void registerHandler(String commandName, Command<TRequest, TResponse> handler) {
//        synchronized (commands) {
//            commands.put(commandName, handler);
//        }
//
//    }
//
//    @Override
//    public void unregisterHandler(String commandName) {
//        synchronized (commands) {
//            commands.remove(commandName);
//        }
//    }

    @Override
    public void start() {
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
        return applicationContext.getBean(commandName, Command.class);
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
            CommandRequestMessage requestMessage = messageSerializer.deserialize(message, CommandRequestMessage.class);
            Command<?,?> command = getCommand(requestMessage.getCommandName());
            //noinspection ConstantValue
            if (command == null) {
                logger.error("Command not found: {}", requestMessage.getCommandName());
                CommandResponseMessage responseMessage = new CommandResponseMessage();
                responseMessage.setErrorMessage("Command not found: " + requestMessage.getCommandName());
                responseMessage.setSuccess(false);
                messageProducer.send(requestMessage.getResponseTopic(), key, responseMessage);
                return;
            }

            Object request = messageSerializer.deserialize(requestMessage.getPayload(), command.getRequestType());
            try {
                Object response = executeCommand(command, request);
                CommandResponseMessage responseMessage = new CommandResponseMessage();
                responseMessage.setSuccess(true);
                String responseJson = messageSerializer.serialize(response);
                responseMessage.setPayload(responseJson);
                messageProducer.send(requestMessage.getResponseTopic(), key, responseMessage);
                logger.info("Sent response message to topic: {} with key: {}", requestMessage.getResponseTopic(), key);
            } catch (Exception e) {
                logger.error("Error handling message", e);
                CommandResponseMessage responseMessage = new CommandResponseMessage();
                responseMessage.setErrorMessage(e.getMessage());
                responseMessage.setSuccess(false);
                messageProducer.send(requestMessage.getResponseTopic(), key, responseMessage);
            }
        } catch (MessageQueueException e) {
            logger.error("Error handling message", e);
        }
    }
}
