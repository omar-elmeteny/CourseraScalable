package com.guctechie.users.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class LockAccountRequest {
    private int userId;
    private String username;
    @NotBlank(message = "Reason cannot be empty")
    private String reason;
    @NotBlank(message = "Lockout time cannot be empty")
    private Timestamp lockoutTime;
}
