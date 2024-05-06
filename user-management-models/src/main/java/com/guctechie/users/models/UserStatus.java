package com.guctechie.users.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class UserStatus {
    private int userId;
    private String username;
    private boolean isEmailVerified;
    private Date registrationDate;
    private boolean isDeleted;
    private boolean isLocked;
    private String lockReason;
    private Timestamp lockoutExpires;
    private int failedLoginCount;
    private ArrayList<String> roles;
    private String passwordHash;
}
