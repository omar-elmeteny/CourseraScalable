package com.guctechie.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
