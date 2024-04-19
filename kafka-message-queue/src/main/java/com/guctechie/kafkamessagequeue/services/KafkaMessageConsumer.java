package com.guctechie.kafkamessagequeue.services;

import com.guctechie.kafkamessagequeue.configs.KafkaConfiguration;
import com.guctechie.messagequeue.services.MessageConsumer;
import com.guctechie.messagequeue.services.MessageObserver;
import com.guctechie.messagequeue.services.Unsubscriber;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;

@Service
public class KafkaMessageConsumer implements MessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class.getName());
    private final ArrayList<Subscription> subscriptions = new ArrayList<>();
    private final ArrayList<String> topics = new ArrayList<>();
    private final KafkaConsumer<String, String> consumer;
    private final Thread consumerThread;
    private volatile boolean running = true;

    public KafkaMessageConsumer(KafkaConfiguration kafkaConfiguration) {
        // Create the consumer
        logger.info("Creating Kafka consumer");
        consumer = new KafkaConsumer<>(kafkaConfiguration.createKafkaProperties());
        logger.info("Kafka consumer created");
        consumerThread = new Thread(this::run);
        consumerThread.start();
    }

    @Override
    public Unsubscriber subscribe(String topic, MessageObserver observer) {
        synchronized (subscriptions) {
            Subscription subscription = new Subscription(topic, observer);
            subscriptions.add(subscription);
            if (!topics.contains(topic)) {
                topics.add(topic);
                consumer.subscribe(topics);
            }
            return () -> removeSubscription(subscription);
        }
    }

    private void removeSubscription(Subscription subscription) {
        synchronized (subscriptions) {
            subscriptions.remove(subscription);
            if (subscriptions.stream().noneMatch(s -> s.getTopic().equals(subscription.getTopic()))) {
                topics.remove(subscription.getTopic());
                consumer.subscribe(topics);
            }
        }
    }

    @PreDestroy
    public void dispose() {
        running = false;
        try {
            consumerThread.join();
        } catch (InterruptedException e) {
            logger.warn("Error while stopping Kafka consumer thread: {}", e.getMessage());
        }
        // Close the consumer
        logger.info("Closing Kafka consumer");
        consumer.close();
    }

    public void run() {
        logger.info("Starting Kafka consumer thread");

        while (running) {
            consumer.poll(Duration.ofMillis(100)).forEach(record -> {
                String topic = record.topic();
                String key = record.key();
                logger.info("Received message from topic: {} with key: {}", record.topic(), record.key());
                synchronized (subscriptions) {
                    subscriptions.stream()
                            .filter(s -> s.getTopic().equals(topic))
                            .forEach(s -> s.getObserver().update(topic, key, record.value()));
                }
            });
        }
        logger.info("Kafka consumer thread stopped");
    }

    @Getter
    @AllArgsConstructor
    private static class Subscription {
        private final String topic;
        private final MessageObserver observer;
    }
}
