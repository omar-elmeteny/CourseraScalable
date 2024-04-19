package com.guctechie.messages.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guctechie.message-queue")
@Getter
@Setter
public class MessageQueueConfig {
    private String commandsTopic;
}
