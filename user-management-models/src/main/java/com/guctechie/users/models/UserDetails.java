package com.guctechie.users.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class UserDetails {
    private int userId;
    private String username;
    private String email;
    private String fullName;
    private String profilePhotoUrl;
    private String phoneNumber;
    private boolean emailVerified;
    private boolean phoneVerified;
    private Date dateOfBirth;
    private Date registrationDate;
    private String passwordHash;
}
