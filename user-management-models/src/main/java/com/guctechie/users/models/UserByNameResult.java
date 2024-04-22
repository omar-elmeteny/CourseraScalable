package com.guctechie.users.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class UserByNameResult {
    private UserDetails userDetails;
}
