package com.guctechie.web.users.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {
    protected final Logger logger;

    protected BaseController() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected ResponseEntity<Object> commandError(String commandName) {
        logger.error("An error occurred while processing the {} command.", commandName);
        return ResponseEntity.internalServerError().body("An error occurred while processing your request.");
    }
}
