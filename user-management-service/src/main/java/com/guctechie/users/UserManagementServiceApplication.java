package com.guctechie.users;

import com.guctechie.messages.services.CommandHandler;
import com.guctechie.messages.services.MessageConsumer;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {
        "com.guctechie.users",
        "com.guctechie.messages",
        "com.guctechie.messages.kafka",
})
public class UserManagementServiceApplication {
    private final MessageConsumer messageConsumer;
    private final CommandHandler commandHandler;

    public UserManagementServiceApplication(
            MessageConsumer messageConsumer,
            CommandHandler commandHandler
    ) {
        this.messageConsumer = messageConsumer;
        this.commandHandler = commandHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        messageConsumer.start();
        commandHandler.start();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
