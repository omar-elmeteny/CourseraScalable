package com.guctechie.users.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private Date dateOfBirth;
    private Date registrationDate;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private String profilePhotoUrl;
    private String phoneNumber;
}
