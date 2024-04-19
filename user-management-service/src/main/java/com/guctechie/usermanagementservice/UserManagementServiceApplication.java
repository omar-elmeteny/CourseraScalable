package com.guctechie.usermanagementservice;

import com.guctechie.messagequeue.services.Command;
import com.guctechie.messagequeue.services.CommandHandler;
import com.guctechie.messagequeue.services.MessageConsumer;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@SpringBootApplication(scanBasePackages = {
        "com.guctechie.usermanagementservice",
        "com.guctechie.kafkamessagequeue",
        "com.guctechie.messagequeue",
})
public class UserManagementServiceApplication {
    private final MessageConsumer messageConsumer;
    private final CommandHandler commandHandler;
    private final ApplicationContext applicationContext;
    @SuppressWarnings("rawtypes")
    private Map<String, Command> commands;

    public UserManagementServiceApplication(
            MessageConsumer messageConsumer,
            CommandHandler commandHandler,
            ApplicationContext applicationContext
    ) {
        this.messageConsumer = messageConsumer;
        this.commandHandler = commandHandler;
        this.applicationContext = applicationContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }

    @PostConstruct
    public void init() {
        messageConsumer.start();
        commandHandler.start();

        commands = applicationContext.getBeansOfType(Command.class);
        for (var entry : commands.entrySet()) {
            Command<?, ?> command = entry.getValue();
            commandHandler.registerHandler(command.getCommandName(), command);
        }
    }

    @PreDestroy
    public void destroy() {
        if (commands != null) {
            for (var entry : commands.entrySet()) {
                Command<?, ?> command = entry.getValue();
                commandHandler.unregisterHandler(command.getCommandName());
            }
        }
    }
}
