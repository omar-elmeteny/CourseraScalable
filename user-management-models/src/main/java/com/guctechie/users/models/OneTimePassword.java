package com.guctechie.users.models;


import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Jacksonized
public class OneTimePassword {
    private int requestId;
    private int userId;
    private String passwordHash;
}
