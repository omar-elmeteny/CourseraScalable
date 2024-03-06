package com.guctechie.datainsertions.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "guctechie.data-insertions")
@Getter
@Setter
public class AppConfig {
    private int userCount;
}
