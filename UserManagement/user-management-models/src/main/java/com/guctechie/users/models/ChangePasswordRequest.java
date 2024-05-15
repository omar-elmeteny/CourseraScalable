package com.guctechie.users.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class ChangePasswordRequest {

    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;
    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
