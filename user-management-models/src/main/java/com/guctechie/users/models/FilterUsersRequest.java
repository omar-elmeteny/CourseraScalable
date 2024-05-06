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

    @Size(max = 50,message = "First name must be between 0 and 50 characters")
    private String firstName;
    @Size(max = 50,message = "Last name must be between 0 and 50 characters")
    private String lastName;
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50, message = "Email must be at most 50 characters long")
    @Email(message = "Invalid email address")
    private String email;
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    @Min(value = 0, message = "Minimum page is 0")
    private int page;
    @Min(value = 1, message = "Minimum page size is 1")
    @Max(value = 100, message = "Maximum page size is 100")
    private int pageSize;

}
