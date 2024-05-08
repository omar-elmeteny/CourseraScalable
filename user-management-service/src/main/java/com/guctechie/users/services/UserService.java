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

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;
    private final MailService mailService;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
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

        if (userRepository.phoneExists(request.getPhoneNumber())) {
            var messages = new ArrayList<String>();
            messages.add("Phone number already exists");
            return RegistrationResult.builder()
                    .successful(false)
                    .validationMessages(messages)
                    .build();
        }

        UserProfileData user = UserProfileData.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .profilePhotoUrl(request.getProfilePhotoUrl())
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.insertUser(user, passwordEncoder.encode(request.getPassword()));
        for (int i = 0; i < request.getRoles().size(); i++) {
            userRepository.assignRoleToUser(user.getUserId(), request.getRoles().get(i));
        }
        mailService.sendWelcomeEmail(user);

        return RegistrationResult.builder()
                .successful(true)
                .username(user.getUsername())
                .userId(user.getUserId())
                .build();
    }

    public UserProfileData findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public LockAccountResult lockAccount(LockAccountRequest request) {
        UserStatus user = userRepository.getUserStatus(request.getUserId());
        if (user == null) {
            String messages = "User not found";
            return LockAccountResult.builder()
                    .successful(false)
                    .errorMessage(messages)
                    .build();
        }
        if (user.getRoles().contains("admin")) {
            String messages = "Cannot lock an admin account";
            return LockAccountResult.builder()
                    .successful(false)
                    .errorMessage(messages)
                    .build();
        }

        userRepository.lockAccount(user.getUserId(), request.getReason(), request.getLockoutTime());
        return LockAccountResult.builder()
                .successful(true)
                .build();
    }

    public LockAccountResult unlockAccount(UnlockAccountRequest request) {
        UserStatus user = userRepository.getUserStatus(request.getUserId());
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

    public String isAuthenticUser(String username, String password, String ipAddress, String userAgent) {
        UserProfileData user = userRepository.findUserByUsername(username);
        if (user == null) {
            logger.warn("bad login attempt by {}: user not found", username);
            return "Invalid username or password";
        }
        UserStatus userStatus = userRepository.getUserStatus(user.getUserId());
        if (userStatus.isLocked()) {
            logger.warn("bad login attempt by {}: account is locked", username);
            return "Account is locked";
        }
        if (userStatus.isDeleted()) {
            logger.warn("bad login attempt by {}: account is deleted", username);
            return "Invalid username or password";
        }
        if (!userStatus.isEmailVerified()) {
            logger.warn("bad login attempt by {}: email not verified", username);
            return "Invalid username or password";
        }

        var result = passwordEncoder.matches(password, userStatus.getPasswordHash());
        if (!result) {
            int attempts = userRepository.addLoginAttempt(user.getUserId(), false, ipAddress, userAgent);
            if (attempts >= 5) {
                userRepository.lockAccount(user.getUserId(), "Too many failed login attempts", null);
            }
            logger.warn("bad login attempt by {}: password mismatch", username);
            return "Invalid username or password";
        } else {
            userRepository.addLoginAttempt(user.getUserId(), true, ipAddress, userAgent);
            logger.info("user {} logged in", username);
            return null;
        }
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

        UserProfileData user = userRepository.findUserByUsername(request.getUsername());
        if (user == null) {
            var messages = new ArrayList<String>();
            messages.add("User not found");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        UserStatus userStatus = userRepository.getUserStatus(user.getUserId());
        if (userStatus.isLocked()) {
            var messages = new ArrayList<String>();
            messages.add("Account is locked");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }


        if (!passwordEncoder.matches(request.getOldPassword(), userStatus.getPasswordHash())) {
            var messages = new ArrayList<String>();
            messages.add("Old password is incorrect");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        if (request.getOldPassword().equals(request.getNewPassword())) {
            var messages = new ArrayList<String>();
            messages.add("Old password and new password cannot be the same");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        userRepository.updatePassword(user.getUserId(), newPasswordHash);
        return ChangePasswordResult.builder()
                .successful(true)
                .validationError(new ArrayList<>())
                .build();
    }

    public ForgotPasswordResult forgotPassword(ForgotPasswordRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            var messages = new ArrayList<String>();
            violations.forEach(violation -> messages.add(violation.getMessage()));
            return ForgotPasswordResult.builder()
                    .successful(false)
                    .message(messages.get(0))
                    .build();
        }
        UserProfileData user = userRepository.findUserByEmail(request.getEmail());
        if (user != null) {
            SecureRandom random = new SecureRandom();
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder otp = new StringBuilder();
            for (int i = 0; i < 12; i++) {
                int index = random.nextInt(characters.length());
                otp.append(characters.charAt(index));
            }
            String passwordHash = passwordEncoder.encode(otp.toString());
            Date expiryDate = new Date(System.currentTimeMillis() + 1800);
            userRepository.createPasswordResetRequest(user.getUserId(), passwordHash, expiryDate);
            //sendEmailWithOTP(request.getEmail(), otp.toString());
            return ForgotPasswordResult.builder()
                    .successful(true)
                    .message("An email has been sent to you with a one-time password")
                    .build();
        }
        return ForgotPasswordResult.builder()
                .successful(true)
                .build();
    }

    public ChangePasswordResult resetPassword(ResetPasswordRequest request) {
        UserStatus user = userRepository.getUserStatus(request.getUserId());
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
        if (passwordEncoder.matches(oldPassword, request.getPassword())) {
            var messages = new ArrayList<String>();
            messages.add("Old password and new password cannot be the same");
            return ChangePasswordResult.builder()
                    .successful(false)
                    .validationError(messages)
                    .build();
        }

        user.setPasswordHash(newPassword);
        userRepository.updatePassword(user.getUserId(), newPassword);
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

    public VerificationResult verifyEmail(VerificationRequest request) {
        var violations = validator.validate(request);
        if (!violations.isEmpty()) {
            var messages = new ArrayList<String>();
            violations.forEach(violation -> messages.add(violation.getMessage()));
            return VerificationResult.builder()
                    .successful(false)
                    .errorMessages(messages)
                    .build();
        }
        UserProfileData user = userRepository.findUserByEmail(request.getEmail());
        if (user == null) {
            var messages = new ArrayList<String>();
            messages.add("User not found");
            return VerificationResult.builder()
                    .successful(false)
                    .errorMessages(messages)
                    .build();
        }
        UserStatus userStatus = userRepository.getUserStatus(user.getUserId());
        if (userStatus.isEmailVerified()) {
            var messages = new ArrayList<String>();
            messages.add("Email already verified");
            return VerificationResult.builder()
                    .successful(false)
                    .errorMessages(messages)
                    .build();
        }
//        if(!passwordEncoder.matches(request.getOtp(), userStatus.getPasswordHash())){
//            userRepository.verifyEmail(user.getUserId());
//            return VerificationResult.builder()
//                    .successful(true)
//                    .errorMessages(new ArrayList<>())
//                    .build();
//        }
//        userRepository.verifyEmail(user.getUserId());
        return VerificationResult.builder()
                .successful(true)
                .build();
    }
}
