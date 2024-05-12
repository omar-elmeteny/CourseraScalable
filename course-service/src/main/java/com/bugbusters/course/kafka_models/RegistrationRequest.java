package com.bugbusters.course.kafka_models;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.sql.Date;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class RegistrationRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 4, max = 30, message = "Username must be between 4 and 20 characters long")
    private String username;
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50, message = "Email must be at most 50 characters long")
    @Email(message = "Invalid email address")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotBlank(message = "First name cannot be empty")
    @Size(min = 4, max = 50, message = "First name must be between 4 and 50 characters long")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 4, max = 50, message = "Last name must be between 4 and 50 characters long")
    private String lastName;
    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth cannot be empty")
    private Date dateOfBirth;
    @Pattern(regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)", message = "Invalid Url")
    @Size(max = 255, message = "Profile photo URL must be at most 255 characters long")
    private String profilePhotoUrl;
    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Invalid phone number")
    @Size(max = 15, message = "Phone number must be at most 15 characters long")
    private String phoneNumber;
    private ArrayList<String> roles = new ArrayList<>();
}
