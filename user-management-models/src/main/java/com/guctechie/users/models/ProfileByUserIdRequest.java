package com.guctechie.users.models;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileByUserIdRequest {
    private int userId;
}
