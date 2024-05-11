package com.guctechie.web;

import com.guctechie.messages.services.MessageConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
@SpringBootApplication(scanBasePackages = {
        "com.guctechie.web",
        "com.guctechie.messages",
})
@EnableCaching
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
