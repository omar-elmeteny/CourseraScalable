package com.guctechie.users.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class AuthenticationRequest {
    public String username;
    public String password;
    public String ipAddress;
    public String userAgent;
}
