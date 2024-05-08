package com.guctechie.users.models;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class ForgotPasswordRequest {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
}
