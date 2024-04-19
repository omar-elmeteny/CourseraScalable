package com.guctechie.messages.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
public class CommandResponseMessage {
    private String payload;
    private boolean success;
    private String errorMessage;
}
