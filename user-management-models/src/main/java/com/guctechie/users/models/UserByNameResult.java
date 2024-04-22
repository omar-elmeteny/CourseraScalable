package com.guctechie.users.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserByNameResult {
    private UserDetails userDetails;
}
