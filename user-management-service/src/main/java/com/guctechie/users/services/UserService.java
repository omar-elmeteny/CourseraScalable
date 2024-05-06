package com.guctechie.users.services;

import com.guctechie.users.models.*;
import com.guctechie.users.repositories.UserRepository;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    public RegistrationResult registerUser(RegistrationRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            var messages = new ArrayList<String>();
            violations.forEach(violation -> messages.add(violation.getMessage()));
            return RegistrationResult.builder()
                    .successful(false)
                    .validationMessages(messages)
                    .build();
        }

        if (userRepository.usernameExists(request.getUsername())) {
            var messages = new ArrayList<String>();
            messages.add("Username already exists");
            return RegistrationResult.builder()
                    .successful(false)
                    .validationMessages(messages)
                    .build();
        }

        if (userRepository.emailExists(request.getEmail())) {
            var messages = new ArrayList<String>();
            messages.add("Email already exists");
            return RegistrationResult.builder()
                    .successful(false)
                    .validationMessages(messages)
                    .build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .isEmailVerified(false)
                .registrationDate(new java.sql.Date(System.currentTimeMillis()))
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.insertUser(user);
        for(int i = 0;i < request.getRoles().size();i++){
            userRepository.assignRoleToUser(user.getUserId(), request.getRoles().get(i));
        }
        return RegistrationResult.builder()
                .successful(true)
                .username(user.getUsername())
                .userId(user.getUserId())
                .build();
    }

    public UserStatus findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        ArrayList<String> roles = userRepository.getUserRoles(user.getUserId());
        return UserStatus.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .isEmailVerified(user.isEmailVerified())
                .registrationDate(user.getRegistrationDate())
                .isDeleted(user.isDeleted())
                .isLocked(user.isLocked())
                .lockReason(user.getLockReason())
                .lockoutExpires(user.getLockoutExpires())
                .failedLoginCount(user.getFailedLoginCount())
                .roles(roles)
                .build();
    }

    public LockAccountResult lockAccount(LockAccountRequest request) {
        User user = userRepository.findUserById(request.getUserId());
        if (user == null) {
            String messages = "User not found";
            return LockAccountResult.builder()
                    .successful(false)
                    .errorMessage(messages)
                    .build();
        }
        if(userRepository.getUserRoles(user.getUserId()).contains("admin")){
            String messages = "Cannot lock an admin account";
            return LockAccountResult.builder()
                    .successful(false)
                    .errorMessage(messages)
                    .build();
        }
        User admin = userRepository.findUserByUsername(request.getUsername());
        if(admin.getUserId() == user.getUserId()){
            String messages = "Cannot lock your own account";
            return LockAccountResult.builder()
                    .successful(false)
                    .errorMessage(messages)
                    .build();
        }
        userRepository.lockAccount(user.getUserId(), user.getLockReason(), user.getLockoutExpires());
        return LockAccountResult.builder()
                .successful(true)
                .build();
    }

    public LockAccountResult unlockAccount(UnlockAccountRequest request) {
        User user = userRepository.findUserById(request.getUserId());
        if (user == null) {
            String messages = "User not found";
            return LockAccountResult.builder()
                    .successful(false)
                    .errorMessage(messages)
                    .build();
        }
        userRepository.unlockAccount(user.getUserId());
        return LockAccountResult.builder()
                .successful(true)
                .build();
    }

    public String isAuthenticUser(String username, String password) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            logger.warn("bad login attempt byt {}: user not found", username);
            return null;
        }
        var result = passwordEncoder.matches(password, user.getPasswordHash()) ? user.getUsername() : null;
        if (result == null) {
            logger.warn("bad login attempt by {}: password mismatch", username);
        } else {
            logger.info("user {} logged in", username);
        }
        return result;
    }

    public ChangePasswordResult changePassword(ChangePasswordRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            var messages = new ArrayList<String>();
            violations.forEach(violation -> messages.add(violation.getMessage()));
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        User user = userRepository.findUserByUsername(request.getUsername());
        if (user == null) {
            var messages = new ArrayList<String>();
            messages.add("User not found");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            var messages = new ArrayList<String>();
            messages.add("Old password is incorrect");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        if(request.getOldPassword().equals(request.getNewPassword())){
            var messages = new ArrayList<String>();
            messages.add("Old password and new password cannot be the same");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.updatePassword(user);
        return ChangePasswordResult.builder()
                .successful(true)
                .validationError(new ArrayList<>())
                .build();
    }

    public ChangePasswordResult resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findUserById(request.getUserId());
        if (user == null) {
            var messages = new ArrayList<String>();
            messages.add("User not found");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }
        String oldPassword = user.getPasswordHash();
        String newPassword = passwordEncoder.encode(request.getPassword());
        if(passwordEncoder.matches(oldPassword, request.getPassword())){
            var messages = new ArrayList<String>();
            messages.add("Old password and new password cannot be the same");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        user.setPasswordHash(newPassword);
        userRepository.updatePassword(user);
        return ChangePasswordResult.builder()
                .successful(true)
                .validationError(new ArrayList<>())
                .build();
    }

    public UserStatusResult getUserStatus(UserStatusRequest request) {
        UserStatus userStatus = userRepository.getUserStatus(request.getUserId());
        if (userStatus == null) {
            ArrayList<String> messages = new ArrayList<>();
            messages.add("User not found");
            return UserStatusResult.builder()
                    .successful(false)
                    .errorMessages(messages)
                    .build();
        }
        return UserStatusResult.builder()
                .userStatus(userStatus)
                .successful(true)
                .errorMessages(new ArrayList<>())
                .build();
    }

    @PreDestroy
    public void destroy() {
        logger.info("Shutting down user service");

    }
}
