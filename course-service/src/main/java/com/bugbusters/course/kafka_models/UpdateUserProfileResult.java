package com.bugbusters.course.kafka_models;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class UpdateUserProfileResult {
    private boolean successful;
    private ArrayList<String> errorMessages;
}
