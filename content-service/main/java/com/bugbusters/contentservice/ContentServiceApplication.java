package com.bugbusters.contentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bugbusters.contentservice.messages.services.CommandHandler;
import com.bugbusters.contentservice.messages.services.MessageConsumer;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ContentServiceApplication {

	private final MessageConsumer messageConsumer;
	private final CommandHandler commandHandler;

	public ContentServiceApplication(
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
		SpringApplication.run(ContentServiceApplication.class, args);
	}
}
