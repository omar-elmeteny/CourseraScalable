package profilemanagement.services;// UserProfileService.java

import profilemanagement.dao.UserProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import profilemanagement.models.UserProfile;
import profilemanagement.repositories.UserProfileRepository;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserProfileService {
    @Autowired
    private final UserProfileRepository userProfileRepository;


    public void insertUserProfile(UserProfileRequest userProfile) {
         UserProfile userProfile1 =  findUserProfileByUserId(Math.toIntExact(userProfile.getUserId()));

         if(userProfile1 != null){
             throw new DataIntegrityViolationException("each user have a unique Profile");
         }

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

    public boolean updateProfile(Integer profileId, UserProfile updatedProfile) {
        UserProfile userProfile = findUserProfileByProfileId(profileId);
        if (userProfile !=null) {

            userProfile.setBio(updatedProfile.getBio());
            userProfile.setFirstName(updatedProfile.getFirstName());
            userProfile.setLastName(updatedProfile.getLastName());
            userProfile.setProfilePhotoUrl(updatedProfile.getProfilePhotoUrl());
            userProfile.setIsEmailVerified(updatedProfile.getIsEmailVerified());
            userProfile.setIsPhoneVerified(updatedProfile.getIsPhoneVerified());
            userProfile.setPhoneNumber(updatedProfile.getPhoneNumber());
            userProfile.setDateOfBirth(updatedProfile.getDateOfBirth());
            userProfileRepository.update(userProfile);

            return true;
        }
        return false;
    }

    public void deleteUserProfile(Integer profileId) {

        if(findUserProfileByProfileId(profileId) == null){
            throw new DataIntegrityViolationException("User profile not found by this profile id: " + profileId);
        }


        userProfileRepository.deleteByProfileId(Long.valueOf(profileId));
    }

    public Page<UserProfile> findAllUsersByFilters(Integer userId,
            String firstName, String lastName, Boolean isEmailVerified, Boolean isPhoneVerified, String phoneNumber, String dateOfBirth, Pageable pageable) {
        LocalDate dateOfBirthDate = dateOfBirth != null ? LocalDate.parse(dateOfBirth) : null;
        return userProfileRepository.findUsersByFilters(userId, firstName, lastName, isEmailVerified, isPhoneVerified, phoneNumber, dateOfBirthDate, pageable);
    }

    public UserProfile findUserProfileByUserId(Integer userId) {
        return userProfileRepository.findUserProfileByUserId(userId);
    }
    public UserProfile findUserProfileByProfileId(Integer profileId) {
        return userProfileRepository.findUserProfileByProfileId(profileId);
    }


    public void deleteUserProfileByUserId(Integer userId) {

            if(findUserProfileByUserId(userId) == null){
                throw new DataIntegrityViolationException("User profile not found by this user id: " + userId);
            }
            userProfileRepository.deleteByUserId(Long.valueOf(userId));

    }
}
