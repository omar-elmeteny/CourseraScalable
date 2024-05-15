package com.guctechie.users.models;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class ForgotPasswordResult {
    private boolean successful;
    private String message;
}
