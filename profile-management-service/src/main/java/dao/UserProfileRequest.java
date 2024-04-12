package dao;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfileRequest implements Serializable {

    private Long userId;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePhotoUrl;
    private Boolean isEmailVerified;
    private Boolean isPhoneVerified;
    private String phoneNumber;
    private String dateOfBirth;


}
