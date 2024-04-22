package com.guctechie.users.services;

import com.guctechie.users.models.RegistrationRequest;
import com.guctechie.users.models.RegistrationResult;
import com.guctechie.users.models.User;
import com.guctechie.users.models.UserInfo;
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
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .registrationDate(new java.sql.Date(System.currentTimeMillis()))
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.insertUser(user);
        return RegistrationResult.builder()
                .successful(true)
                .username(user.getUsername())
                .userId(user.getUserId())
                .build();
    }

    public UserInfo findUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            return null;
        }
        ArrayList<String> roles = userRepository.getUserRoles(user.getUserId());
        return UserInfo.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .phoneNumber(user.getPhoneNumber())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .dateOfBirth(user.getDateOfBirth())
                .registrationDate(user.getRegistrationDate())
                .passwordHash(user.getPasswordHash())
                .roles(roles)
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

    @PreDestroy
    public void destroy() {
        logger.info("Shutting down user service");

    }
}
