package profilemanagement.services;// UserProfileService.java

import dao.UserProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import profilemanagement.models.UserProfile;
import profilemanagement.repositories.UserProfileRepository;

import java.time.LocalDate;


import org.springframework.data.redis.core.RedisTemplate;


@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Autowired
    private final UserProfileRepository userProfileRepository;


    public void insertUserProfile(UserProfileRequest userProfile) {
        userProfileRepository.insertUserProfile(userProfile);
    }

    public String handleConstraintViolationException(DataIntegrityViolationException e) {
        String message = e.getLocalizedMessage();

        // Customize error message based on the specific constraint violation
        if (message.contains("unique_username_constraint")) {
            return "Username already exists";
        } else if (message.contains("fk_user_profile_address_id")) {
            return "Invalid address ID provided";
        } else {
            return "Constraint violation occurred: " + message;
        }
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
