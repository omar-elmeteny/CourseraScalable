package com.bugbusters.course.messages.services;

import com.bugbusters.course.messages.configs.MessageQueueConfig;
import com.bugbusters.course.messages.configs.WebServerConfig;
import com.bugbusters.course.messages.exceptions.MessageQueueException;
import com.bugbusters.course.messages.models.CommandRequestMessage;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class MessageQueueCommandDispatcher implements CommandDispatcher {

    private final MessageProducer messageProducer;
    private final MessageSerializer messageSerializer;
    private final MessageQueueConfig messageQueueConfig;
    private final WebServerConfig webServerConfig;

    public MessageQueueCommandDispatcher(
            MessageConsumer messageConsumer,
            MessageProducer messageProducer,
            MessageSerializer messageSerializer,
            MessageQueueConfig messageQueueConfig,
            WebServerConfig webServerConfig) {

        this.messageProducer = messageProducer;
        this.messageSerializer = messageSerializer;
        this.messageQueueConfig = messageQueueConfig;
        this.webServerConfig = webServerConfig;

    }

    @Override
    public <TRequest> void sendCommand(String commandName, TRequest request) throws MessageQueueException {
        CommandRequestMessage commandRequestMessage = new CommandRequestMessage();
        commandRequestMessage.setCommandName(commandName);
        commandRequestMessage.setPayload(messageSerializer.serialize(request));
        commandRequestMessage.setResponseTopic(webServerConfig.getResponseTopic());

        UUID key = UUID.randomUUID();

        String topicName = messageQueueConfig.getTopics().get(commandName);
        messageProducer.send(topicName, key.toString(), commandRequestMessage);
    }
}
