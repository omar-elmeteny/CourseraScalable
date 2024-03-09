package profilemanagement.services;// UserProfileService.java

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import profilemanagement.models.UserProfile;
import profilemanagement.repositories.UserProfileRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Autowired
    private final UserProfileRepository userProfileRepository;


    public void insertUserProfile(UserProfile userProfile) {
        userProfileRepository.insertUserProfile(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.updateUserProfile(userProfile);
    }

    public void deleteUserProfile(Long profileId) {
        userProfileRepository.deleteByUserId(profileId);
    }

    public Page<UserProfile> findAllUsersByFilters(Integer userId,
            String firstName, String lastName, Boolean isEmailVerified, Boolean isPhoneVerified, String phoneNumber, String dateOfBirth, Pageable pageable) {
        LocalDate dateOfBirthDate = dateOfBirth != null ? LocalDate.parse(dateOfBirth) : null;
        return userProfileRepository.findUsersByFilters(userId, firstName, lastName, isEmailVerified, isPhoneVerified, phoneNumber, dateOfBirthDate, pageable);
    }


}
