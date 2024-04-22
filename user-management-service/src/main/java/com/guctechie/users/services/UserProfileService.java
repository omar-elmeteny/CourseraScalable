package com.guctechie.users.services;// UserProfileService.java

import com.guctechie.users.models.*;
import com.guctechie.users.repositories.UserProfileRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final Validator validator;

    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }


    public InsertUserProfileResult insertUserProfile(UserProfileData userProfile) throws Exception {
        var violations = validator.validate(userProfile);
        if (!violations.isEmpty()) {
            var result = new InsertUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            violations.forEach(violation -> result.getErrorMessages().add(violation.getMessage()));
            return result;
        }
        UserProfileData userProfile1 = findUserProfileByUserId(userProfile.getUserId());

        if (userProfile1 != null) {
            var result = new InsertUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            result.getErrorMessages().add("User profile already exists for this user id.");
            return result;
        }

        userProfile1 = findUserProfileByPhone(userProfile.getPhoneNumber());

        if (userProfile1 != null) {
            var result = new InsertUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            result.getErrorMessages().add("User profile already exists for this phone number.");
            return result;
        }

        int profileId = userProfileRepository.insertUserProfile(userProfile);
        var result = new InsertUserProfileResult();
        result.setSuccessful(true);
        result.setProfileId(profileId);
        return result;
    }

    public UpdateUserProfileResult updateProfile(Integer profileId, UserProfileData updatedProfile) {
        var violations = validator.validate(updatedProfile);
        if (!violations.isEmpty()) {
            var result = new UpdateUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            violations.forEach(violation -> result.getErrorMessages().add(violation.getMessage()));
            return result;
        }
        UserProfileData userProfile = findUserProfileByProfileId(profileId);
        if (userProfile == null) {
            var result = new UpdateUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            result.getErrorMessages().add("User profile not found by this profile id");
            return result;
        }

        UserProfileData userProfile2 = findUserProfileByPhone(updatedProfile.getPhoneNumber());
        if (userProfile2 != null && userProfile2.getProfileId() != profileId) {
            var result = new UpdateUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            result.getErrorMessages().add("User profile already exists for this phone number.");
            return result;
        }

        userProfileRepository.update(updatedProfile);
        var result = new UpdateUserProfileResult();
        result.setSuccessful(true);
        return result;
    }

    public boolean deleteUserProfile(int profileId) {
        if (findUserProfileByProfileId(profileId) == null) {
            return false;
        }

        userProfileRepository.deleteByProfileId(profileId);
        return true;
    }

    public ProfilesResult findAllUsersByFilters(FilterProfilesRequest filterProfilesRequest) {
        Set<ConstraintViolation<FilterProfilesRequest>> violations = validator.validate(filterProfilesRequest);
        if (!violations.isEmpty()) {
            ProfilesResult result = new ProfilesResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            violations.forEach(violation -> result.getErrorMessages().add(violation.getMessage()));
            return result;
        }

        int limit = filterProfilesRequest.getPageSize();
        int offset = filterProfilesRequest.getPage() * limit;
        List<UserProfileData> result = userProfileRepository.findUsersByFilters(
                filterProfilesRequest.getUserId(),
                filterProfilesRequest.getFirstName(),
                filterProfilesRequest.getLastName(),

                filterProfilesRequest.getEmailVerified(),
                filterProfilesRequest.getPhoneVerified(),
                filterProfilesRequest.getPhoneNumber(),
                filterProfilesRequest.getDateOfBirth(),
                limit,
                offset);

        int totalNumberOfProfiles = userProfileRepository.countUsersByFilters(
                filterProfilesRequest.getUserId(),
                filterProfilesRequest.getFirstName(),
                filterProfilesRequest.getLastName(),
                filterProfilesRequest.getEmailVerified(),
                filterProfilesRequest.getPhoneVerified(),
                filterProfilesRequest.getPhoneNumber(),
                filterProfilesRequest.getDateOfBirth()
        );

        ProfilesResult profilesResult = new ProfilesResult();
        profilesResult.setTotalNumberOfProfiles(totalNumberOfProfiles);
        profilesResult.setProfiles(new ArrayList<>(result));
        profilesResult.setSuccessful(true);
        return profilesResult;
    }

    public UserProfileData findUserProfileByUserId(Integer userId) {
        return userProfileRepository.findUserProfileByUserId(userId);
    }

    public UserProfileData findUserProfileByPhone(String phoneNumber) {
        return userProfileRepository.findUserProfileByPhone(phoneNumber);
    }

    public UserProfileData findUserProfileByProfileId(Integer profileId) {
        return userProfileRepository.findUserProfileByProfileId(profileId);
    }


    public boolean deleteUserProfileByUserId(int userId) {
        UserProfileData profile = findUserProfileByUserId(userId);
        if (profile == null) {
            return false;
        }
        userProfileRepository.deleteByProfileId(profile.getProfileId());
        return true;
    }
}
