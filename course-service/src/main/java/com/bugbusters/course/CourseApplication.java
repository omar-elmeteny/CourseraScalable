package com.bugbusters.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bugbusters.course.messages.services.CommandHandler;
import com.bugbusters.course.messages.services.MessageConsumer;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableEurekaClient
public class CourseApplication {

    private final MessageConsumer messageConsumer;
    private final CommandHandler commandHandler;

    public CourseApplication(
            MessageConsumer messageConsumer,
            CommandHandler commandHandler) {
        this.messageConsumer = messageConsumer;
        this.commandHandler = commandHandler;
    }

    @PostConstruct
    public void init() {
        messageConsumer.start();
        commandHandler.start();
    }

    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, args);
    }
}
