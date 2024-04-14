package com.guctechie.messagequeue.runners;

import com.guctechie.messagequeue.models.AuthenticationRequest;
import com.guctechie.messagequeue.services.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class TestRunner implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(TestRunner.class);
    private final MessageProducer messageProducer;

    public TestRunner(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }


    @Override
    public void run(String... args) throws Exception {
        logger.info("TestRunner started");
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("test-username");
        authenticationRequest.setPassword("test-password");
        messageProducer.send("test-topic-hamada", "test-key", authenticationRequest);
        logger.info("TestRunner finished");
    }
}
