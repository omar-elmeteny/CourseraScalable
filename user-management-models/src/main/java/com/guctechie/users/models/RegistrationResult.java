package com.guctechie.users.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResult {
    private String username;
    private int userId;
    private boolean successful;
    private ArrayList<String> validationMessages = new ArrayList<>();

}
