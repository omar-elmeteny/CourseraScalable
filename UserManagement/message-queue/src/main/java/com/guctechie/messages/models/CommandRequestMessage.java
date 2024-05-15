package com.guctechie.messages.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class CommandRequestMessage {
    private String commandName;
    private String payload;
    private String responseTopic;
    private boolean async;
}
