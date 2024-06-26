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
public class VerificationResult {
    private boolean successful;
    private ArrayList<String> errorMessages;
}
