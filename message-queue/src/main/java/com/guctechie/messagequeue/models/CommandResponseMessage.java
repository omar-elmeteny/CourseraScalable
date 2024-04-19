package com.guctechie.messagequeue.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommandResponseMessage {
    private String payload;
    private String key;
    private boolean success;
    private String errorMessage;
}
