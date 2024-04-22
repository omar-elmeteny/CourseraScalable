package com.guctechie.users.models;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileData {
    private int profileId;
    private int userId;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    @NotBlank
    @NotNull
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    private String bio;

    @Pattern(regexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)", message = "Invalid Url")
    @Size(max = 255, message = "Profile photo URL must be at most 255 characters long")
    private String profilePhotoUrl;

    private boolean emailVerified;
    private boolean phoneVerified;

    @NotNull
    @Pattern(regexp = "^\\+?[0-9]{6,15}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;
}
