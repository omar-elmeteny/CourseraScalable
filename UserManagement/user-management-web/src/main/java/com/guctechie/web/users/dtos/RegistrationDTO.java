package com.guctechie.web.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Jacksonized
@Builder
public class RegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String profilePhotoUrl;
    private String phoneNumber;
}
