package com.guctechie.web.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class UserProfileDTO implements Serializable {
    private int userId;
    private int profileId;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePhotoUrl;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private String phoneNumber;
    private Date dateOfBirth;
}
