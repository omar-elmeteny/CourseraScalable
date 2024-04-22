package com.guctechie.users.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class ProfilesResult {
    private int totalNumberOfProfiles;
    ArrayList<UserProfileData> profiles;
    boolean successful;
    ArrayList<String> errorMessages;
}
