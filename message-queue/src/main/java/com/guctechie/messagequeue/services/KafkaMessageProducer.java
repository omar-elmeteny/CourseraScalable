package com.guctechie.messagequeue.services;

import com.guctechie.messagequeue.configs.KafkaConfiguration;
import com.guctechie.messagequeue.exceptions.MessageQueueException;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.concurrent.Future;

@Service
public class KafkaMessageProducer implements MessageProducer{
    private final Producer<String, String> producer;
    private final MessageSerializer messageSerializer;

    public KafkaMessageProducer(KafkaConfiguration kafkaConfiguration, MessageSerializer messageSerializer) {
        this.messageSerializer = messageSerializer;

        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaConfiguration.getServers());
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // put password
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                + kafkaConfiguration.getUsername() + "\""
                + " password=\"" + kafkaConfiguration.getPassword() + "\";");

        // Create the producer
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
