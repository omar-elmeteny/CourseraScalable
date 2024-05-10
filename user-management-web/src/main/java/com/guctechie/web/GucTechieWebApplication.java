package com.guctechie.web;

import com.guctechie.messages.services.MessageConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@SpringBootApplication(scanBasePackages = {
        "com.guctechie.web",
        "com.guctechie.messages",
})

public class GucTechieWebApplication {
    private final MessageConsumer messageConsumer;

    public GucTechieWebApplication(
            MessageConsumer messageConsumer
    ) {
        this.messageConsumer = messageConsumer;

    }

    public static void main(String[] args) {
        SpringApplication.run(GucTechieWebApplication.class, args);
    }

    @PostConstruct
    public void init() {
        messageConsumer.start();
    }
}
