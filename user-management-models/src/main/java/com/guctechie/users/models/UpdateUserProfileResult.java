package com.guctechie.users.models;

import lombok.*;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateUserProfileResult {
    private boolean successful;
    private ArrayList<String> errorMessages;
}
