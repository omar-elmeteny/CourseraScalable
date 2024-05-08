package com.guctechie.users.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMailModel {
    private String firstName;
    private String lastName;
    private String email;
    private String oneTimePassword;
}
