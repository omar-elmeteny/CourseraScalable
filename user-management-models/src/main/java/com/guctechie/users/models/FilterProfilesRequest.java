package com.guctechie.users.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
@Builder
public class FilterProfilesRequest {
    private Integer userId;

    @Size(max = 50,message = "First name must be between 0 and 50 characters")
    private String firstName;
    @Size(max = 50,message = "Last name must be between 0 and 50 characters")
    private String lastName;
    private String profilePhotoUrl;
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phoneNumber;
    private LocalDate dateOfBirth;
    @Min(value = 0, message = "Minimum page is 0")
    private int page;
    @Min(value = 1, message = "Minimum page size is 1")
    @Max(value = 100, message = "Maximum page size is 100")
    private int pageSize;

    private Boolean emailVerified;
    private Boolean phoneVerified;
}
