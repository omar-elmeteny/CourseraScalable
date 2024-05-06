package com.guctechie.users.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Date registrationDate;
    private boolean isEmailVerified;
    private String profilePhotoUrl;
    private String phoneNumber;
    private String bio;
    private boolean isDeleted;
    private boolean isLocked;
    private String lockReason;
    private Timestamp lockoutExpires;
    private int failedLoginCount;
}
