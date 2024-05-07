package com.guctechie.users.services;// UserProfileService.java

import com.guctechie.users.models.*;
import com.guctechie.users.repositories.UserProfileRepository;
import com.guctechie.users.repositories.UserRepository;
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
    private final UserRepository userRepository;
    private final Validator validator;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public UpdateUserProfileResult updateProfile(UpdateUserProfileRequest request) {
        UserProfileData currentUser = userRepository.findUserByUsername(request.getUsername());
        if(currentUser == null) {
            var result = new UpdateUserProfileResult();
            ArrayList<String> messages = new ArrayList<>();
            messages.add("User not found");
            result.setSuccessful(false);
            result.setErrorMessages(messages);
            return result;
        }
        var violations = validator.validate(request.getUserProfileData());
        if (!violations.isEmpty()) {
            var result = new UpdateUserProfileResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            violations.forEach(violation -> result.getErrorMessages().add(violation.getMessage()));
            return result;
        }

        UserProfileData userProfileData = findUserProfileByPhone(request.getUserProfileData().getPhoneNumber());
        if (userProfileData != null) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("Phone number already exists");
            return UpdateUserProfileResult.builder()
                    .successful(false)
                    .errorMessages(messages)
                    .build();
        }

        userProfileRepository.update(currentUser.getUserId(), request.getUserProfileData());
        var result = new UpdateUserProfileResult();
        result.setSuccessful(true);
        return result;
    }

    public DeleteResult deleteUser(String username) {
        UserProfileData currentUser = userRepository.findUserByUsername(username);
        if (currentUser == null) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("User not found");
            return DeleteResult.builder()
                    .successful(false)
                    .validationErrors(messages)
                    .build();
        }

        int userId = currentUser.getUserId();
        userProfileRepository.deleteUserByUserId(userId);
        return DeleteResult.builder()
                .successful(true)
                .build();
    }


    public DeleteResult deleteAdmin(String username, int userId) {
        UserProfileData currentAdmin = userRepository.findUserByUsername(username);
        if (currentAdmin == null) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("Admin not found");
            return DeleteResult.builder()
                    .successful(false)
                    .validationErrors(messages)
                    .build();
        }
        int adminId = currentAdmin.getUserId();
        if(adminId == userId) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("Admin cannot delete itself");
            return DeleteResult.builder()
                    .successful(false)
                    .validationErrors(messages)
                    .build();
        }
        if(!userRepository.getUserRoles(userId).contains("admin")) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("This user is not an admin");
            return DeleteResult.builder()
                    .successful(false)
                    .validationErrors(messages)
                    .build();
        }
        userRepository.removeRoleFromUser(userId, "admin");
        return DeleteResult.builder()
                .successful(true)
                .build();
    }

    public ProfilesResult findAllUsersByFilters(FilterUsersRequest filterUsersRequest) {
        Set<ConstraintViolation<FilterUsersRequest>> violations = validator.validate(filterUsersRequest);
        if (!violations.isEmpty()) {
            ProfilesResult result = new ProfilesResult();
            result.setSuccessful(false);
            result.setErrorMessages(new ArrayList<>());
            violations.forEach(violation -> result.getErrorMessages().add(violation.getMessage()));
            return result;
        }

        int limit = filterUsersRequest.getPageSize();
        int offset = filterUsersRequest.getPage() * limit;
        List<UserProfileData> result = userProfileRepository.findUsersByFilters(
                filterUsersRequest.getFirstName(),
                filterUsersRequest.getLastName(),
                filterUsersRequest.getEmail(),
                filterUsersRequest.getPhoneNumber(),
                limit,
                offset);

        int totalNumberOfProfiles = userProfileRepository.countUsersByFilters(
                filterUsersRequest.getFirstName(),
                filterUsersRequest.getLastName(),
                filterUsersRequest.getEmail(),
                filterUsersRequest.getPhoneNumber()
        );

        ProfilesResult profilesResult = new ProfilesResult();
        profilesResult.setTotalNumberOfProfiles(totalNumberOfProfiles);
        profilesResult.setProfiles(new ArrayList<>(result));
        profilesResult.setSuccessful(true);
        return profilesResult;
    }

    public UserProfileData findUserProfileByPhone(String phoneNumber) {
        return userProfileRepository.findUserProfileByPhone(phoneNumber);
    }

}
