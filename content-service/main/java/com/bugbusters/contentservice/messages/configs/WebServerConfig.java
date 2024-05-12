package com.bugbusters.contentservice.messages.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guctechie.webserver")
@Getter
@Setter
public class WebServerConfig {
    private String responseTopic = "response-topic";
    private int accessTokenDurationMinutes = 60;
    private int refreshTokenDurationDays = 30;
    private String secret = "secret";
}
