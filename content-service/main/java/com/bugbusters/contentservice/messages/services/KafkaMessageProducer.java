package com.bugbusters.contentservice.messages.services;

import com.bugbusters.contentservice.messages.configs.KafkaConfiguration;
import com.bugbusters.contentservice.messages.exceptions.MessageQueueException;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.Future;

@Service
public class KafkaMessageProducer implements MessageProducer {
    private final Producer<String, String> producer;
    private final MessageSerializer messageSerializer;

    public KafkaMessageProducer(KafkaConfiguration kafkaConfiguration, MessageSerializer messageSerializer) {
        this.messageSerializer = messageSerializer;

        Properties properties = kafkaConfiguration.createKafkaProperties();
        producer = new KafkaProducer<>(properties);
    }

    @Override
    public void send(String topic, String key, Object message) throws MessageQueueException {
        String serializedMessage = messageSerializer.serialize(message);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, serializedMessage);

        Future<RecordMetadata> recordMetadata = producer.send(record);
        try {
            recordMetadata.get();
        } catch (Exception e) {
            throw new MessageQueueException("Error while sending message", e);
        }
    }

    @PreDestroy
    public void dispose() {
        // Close the producer
        producer.close();
    }
}
