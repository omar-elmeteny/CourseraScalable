package com.bugbusters.course.kafka_config.services;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bugbusters.course.kafka_config.configs.KafkaConfiguration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaMessageConsumer implements MessageConsumer {

    private final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class.getName());
    private final ArrayList<Subscription> subscriptions = new ArrayList<>();
    private final ArrayList<String> topics = new ArrayList<>();
    private final KafkaConsumer<String, String> consumer;
    private final Thread consumerThread;
    private final KafkaConfiguration kafkaConfiguration;
    private volatile boolean running;
    private int topicsVersion = 0;

    public KafkaMessageConsumer(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
        // Create the consumer
        logger.info("Creating Kafka consumer");
        consumer = new KafkaConsumer<>(kafkaConfiguration.createKafkaProperties());
        logger.info("Kafka consumer created");
        consumerThread = new Thread(this::run);

    }

    @Override
    public Unsubscriber subscribe(String topic, MessageObserver observer) {
        try {
            synchronized (subscriptions) {
                Subscription subscription = new Subscription(topic, observer);
                subscriptions.add(subscription);
                if (!topics.contains(topic)) {
                    ensureTopicExists(topic);
                    topics.add(topic);
                    // consumer.subscribe(topics);
                    topicsVersion++;
                    subscriptions.notify();
                }
                return () -> removeSubscription(subscription);
            }
        } catch (Exception e) {
            logger.error("Error while subscribing to topic: {}", e.getMessage());
            return () -> {
            };
        }
    }

    private void ensureTopicExists(String topicName) {
        Properties properties = kafkaConfiguration.createKafkaProperties();
        try (AdminClient adminClient = AdminClient.create(properties)) {
            ListTopicsResult listTopicsResult = adminClient.listTopics();
            Set<String> topics = listTopicsResult.names().get();
            if (!topics.contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(Collections.singleton(newTopic));
                logger.info("Topic created: {}", topicName);
            } else {
                logger.info("Topic already exists: {}", topicName);
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Error while creating topic: {}", e.getMessage());
        }
    }

    private void removeSubscription(Subscription subscription) {
        synchronized (subscriptions) {
            subscriptions.remove(subscription);
            if (subscriptions.stream().noneMatch(s -> s.getTopic().equals(subscription.getTopic()))) {

                topics.remove(subscription.getTopic());
                // consumer.subscribe(topics);
                topicsVersion++;
                subscription.notify();
            }
        }
    }

    @Override
    public void start() {
        running = true;
        consumerThread.start();
    }

    @PreDestroy
    public void stop() {
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
        int lastTopicsVersion = -1;
        while (running) {
            synchronized (subscriptions) {
                while (subscriptions.isEmpty()) {
                    try {
                        subscriptions.wait();
                    } catch (InterruptedException e) {
                        logger.warn("Error while waiting for subscriptions: {}", e.getMessage());
                    }
                }
                if (lastTopicsVersion != topicsVersion) {
                    lastTopicsVersion = topicsVersion;
                    consumer.subscribe(topics);
                }

                if (topics.isEmpty()) {
                    continue;
                }
            }
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
