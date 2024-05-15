package com.guctechie.web.users.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class LockRequestDTO {
    private String reason;
    private Timestamp lockoutTime;
}
