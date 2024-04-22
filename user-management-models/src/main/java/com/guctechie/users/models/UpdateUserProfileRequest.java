package com.guctechie.users.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserProfileRequest {
    private int profileId;
    private String username;
    private UserProfileData userProfileData;
}
