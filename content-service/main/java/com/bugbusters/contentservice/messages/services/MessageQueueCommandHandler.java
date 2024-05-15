package com.bugbusters.contentservice.messages.services;

import com.bugbusters.contentservice.messages.configs.MessageQueueConfig;
import com.bugbusters.contentservice.messages.exceptions.MessageQueueException;
import com.bugbusters.contentservice.messages.models.CommandRequestMessage;
import com.bugbusters.contentservice.messages.models.CommandResponseMessage;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private ArrayList<Unsubscriber> unsubscribers;
    private final ExecutorService executor;

    public MessageQueueCommandHandler(
            MessageQueueConfig messageQueueConfig,
            MessageConsumer messageConsumer,
            MessageProducer messageProducer,
            MessageSerializer messageSerializer,
            ApplicationContext applicationContext) {
        this.messageQueueConfig = messageQueueConfig;
        this.messageConsumer = messageConsumer;
        this.messageProducer = messageProducer;
        this.messageSerializer = messageSerializer;
        this.applicationContext = applicationContext;
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public void start() {
        synchronized (this) {
            if (unsubscribers != null) {
                return;
            }
            unsubscribers = new ArrayList<>();
            String[] commandNames = applicationContext.getBeanNamesForType(Command.class);
            ArrayList<String> topics = new ArrayList<>();
            for (String commandName : commandNames) {
                String topicName = messageQueueConfig.getTopics().get(commandName);
                if (topicName == null) {
                    logger.error("Topic not found for command: {}", commandName);
                    continue;
                }
                if (topics.contains(topicName)) {
                    continue;
                }
                topics.add(topicName);
                unsubscribers.add(messageConsumer.subscribe(topicName, this::handleMessageOnThreadPool));
            }
        }
    }

    @PreDestroy()
    public void stop() {
        synchronized (this) {
            if (unsubscribers == null) {
                return;
            }
            for (Unsubscriber unsubscriber : unsubscribers) {
                unsubscriber.unsubscribe();
            }
            unsubscribers = null;
        }

    }

    private Command<?, ?> getCommand(String commandName) {
        return applicationContext.getBean(commandName, Command.class);
    }

    private <TRequest, TResponse> Object executeCommand(Command<TRequest, TResponse> command, Object request)
            throws Exception {
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
            Command<?, ?> command = getCommand(requestMessage.getCommandName());
            // noinspection ConstantValue
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
