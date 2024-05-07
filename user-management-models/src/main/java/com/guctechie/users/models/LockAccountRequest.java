package com.guctechie.users.models;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class LockAccountRequest {
    private int userId;
    @NotBlank(message = "Reason cannot be empty")
    private String reason;
    @NotNull(message = "Lockout time cannot be empty")
    @Future(message = "Lockout time must be in the future")
    private Date lockoutTime;
}
