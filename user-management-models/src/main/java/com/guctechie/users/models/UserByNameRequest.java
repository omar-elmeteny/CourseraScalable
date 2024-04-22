package com.guctechie.users.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserByNameRequest {
    private String username;
}
