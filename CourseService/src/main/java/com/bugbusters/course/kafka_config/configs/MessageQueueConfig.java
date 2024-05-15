package com.bugbusters.course.kafka_config.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "guctechie.message-queue")
@Getter
@Setter
public class MessageQueueConfig {
    private Map<String, String> topics;
    private int commandTimeoutMillis = 30000;
}
