package com.guctechie.web.users.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class UserStatusDTO {
    private int userId;
    private String username;
    private boolean locked;
    private boolean deleted;
    private int failedLoginCount;
    private String lockReason;
    private Date lockoutExpires;
    private boolean emailVerified;
    private Date registrationDate;
    private Date passwordDate;
    private ArrayList<String> roles;
}
