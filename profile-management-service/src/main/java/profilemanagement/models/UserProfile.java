package profilemanagement.models;// UserProfile.java

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "user_profile")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

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
