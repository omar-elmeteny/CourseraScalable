package com.guctechie.users.models;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class LockAccountResult {
    private boolean successful;
    private String errorMessage;
}
