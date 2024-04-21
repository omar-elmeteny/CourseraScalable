package com.guctechie.web.users.dtos;

import authentication.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
<<<<<<<< HEAD:user-management-web/src/main/java/com/guctechie/web/users/dtos/AuthenticationRequestDTO.java
@Jacksonized
@Builder
public class AuthenticationRequestDTO {
========
public class RegisterRequestDto {
>>>>>>>> origin/main:user-management-web/src/main/java/com/guctechie/web/users/dtos/RegisterRequestDto.java
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
