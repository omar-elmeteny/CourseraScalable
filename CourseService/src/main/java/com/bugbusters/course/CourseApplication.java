package com.bugbusters.course;

import com.guctechie.messages.services.CommandHandler;
import com.guctechie.messages.services.MessageConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import jakarta.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = {
        "com.bugbusters.course",
        "com.guctechie.messages"
})@EnableEurekaClient
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
