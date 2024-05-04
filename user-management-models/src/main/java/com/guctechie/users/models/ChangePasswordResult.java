package com.guctechie.users.models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class ChangePasswordResult {
    private boolean successful;
    private ArrayList<String> validationError;
}
