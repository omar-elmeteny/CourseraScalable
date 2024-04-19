package com.guctechie.messagequeue.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandRequestMessage {
    private String commandName;
    private String payload;
    private String key;
    private String responseTopic;
}
