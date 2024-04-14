package com.guctechie.messagequeue.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guctechie.kafka")
@Getter
@Setter
public class KafkaConfiguration {
    private String servers;
    private String username;
    private String password;
}
