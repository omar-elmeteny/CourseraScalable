package com.guctechie.messages.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guctechie.messages.exceptions.MessageQueueException;
import org.springframework.stereotype.Service;

@Service
public class JsonMessageSerializer implements MessageSerializer {

    private final ObjectMapper objectMapper;

    public JsonMessageSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String serialize(Object object) throws MessageQueueException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new MessageQueueException("Error during JSON serialization", e);
        }
    }

    @Override
    public <T> T deserialize(String message, Class<T> type) throws MessageQueueException {
        try {
            return objectMapper.readValue(message, type);
        } catch (JsonProcessingException e) {
            throw new MessageQueueException("Error during JSON deserialization", e);
        }
    }
}
