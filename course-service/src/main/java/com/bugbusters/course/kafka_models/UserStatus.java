package com.bugbusters.course.kafka_models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class UserStatus {
    private int userId;
    private String username;
    private boolean locked;
    private boolean deleted;
    private int failedLoginCount;
    private String lockReason;
    private Date lockoutExpires;
    private boolean emailVerified;
    private Date registrationDate;
    private Date passwordDate;
    private String passwordHash;
    private ArrayList<String> roles;
}
