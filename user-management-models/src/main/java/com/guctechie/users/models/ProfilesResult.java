package com.guctechie.users.models;

import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfilesResult {
    private int totalNumberOfProfiles;
    ArrayList<UserProfileData> profiles;
    boolean successful;
    ArrayList<String> errorMessages;
}
