package com.guctechie.usermanagementweb;

import com.guctechie.messagequeue.services.MessageConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.guctechie.usermanagementweb",
        "com.guctechie.kafkamessagequeue",
        "com.guctechie.messagequeue",
})

public class UserManagementWebApplication {
    private final MessageConsumer messageConsumer;

    public UserManagementWebApplication(
            MessageConsumer messageConsumer
    ) {
        this.messageConsumer = messageConsumer;

    }

    public static void main(String[] args) {
        SpringApplication.run(UserManagementWebApplication.class, args);
    }

    @PostConstruct
    public void init() {
        messageConsumer.start();
    }

}
