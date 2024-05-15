package com.bugbusters.course.kafka_models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResult {
    private boolean authenticated;
    private String username;
    private String message;
}
