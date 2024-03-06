package com.guctechie.datainsertions.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
@Getter
@Setter
public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private Date dateOfBirth;
    private Date registrationDate;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private String profilePhotoUrl;
    private String phoneNumber;

}
