package com.guctechie.UserManagement.admin.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminRegisterationDTO {

    @NotBlank(message = "Username cannot be blank")
    private String username;
    private String email;
    private String fullName;
    private Date dateOfBirth;
    private Date registrationDate;
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    private String profilePhotoUrl;
    private String phoneNumber;
    private String passwordUnhashed;
}
