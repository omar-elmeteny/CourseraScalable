package com.guctechie.users.models;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class FilterUsersRequest {
    private Integer userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @Min(value = 0, message = "Minimum page is 0")
    private int page;
    @Min(value = 1, message = "Minimum page size is 1")
    @Max(value = 100, message = "Maximum page size is 100")
    private int pageSize;

}
