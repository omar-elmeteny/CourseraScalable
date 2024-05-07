package com.guctechie.web.users.controllers;


import com.guctechie.messages.CommandNames;
import com.guctechie.messages.exceptions.MessageQueueException;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.users.models.*;
import com.guctechie.web.config.Admin;
import com.guctechie.web.users.dtos.LockRequestDTO;
import com.guctechie.web.users.dtos.RegistrationDTO;
import com.guctechie.web.users.dtos.ResetPasswordDTO;
import com.guctechie.web.users.dtos.UserProfileDTO;
import com.guctechie.web.utils.SerializablePage;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController extends BaseController {

    private final CommandDispatcher commandDispatcher;

    public AdminController(CommandDispatcher commandDispatcher) {
        super();
        this.commandDispatcher = commandDispatcher;
    }

    private static UserProfileDTO mapUserProfile(UserProfileData userProfileData) {
        return UserProfileDTO.builder()
                .userId(userProfileData.getUserId())
                .firstName(userProfileData.getFirstName())
                .lastName(userProfileData.getLastName())
                .bio(userProfileData.getBio())
                .profilePhotoUrl(userProfileData.getProfilePhotoUrl())
                .phoneNumber(userProfileData.getPhoneNumber())
                .dateOfBirth(userProfileData.getDateOfBirth())
                .build();
    }

    @Admin
    @PostMapping("create-admin")
    public ResponseEntity<Object> createAdmin(@RequestBody RegistrationDTO registrationDTO) {
        try {
            ArrayList<String> roles = new ArrayList<>();
            roles.add("student");
            roles.add("admin");
            RegistrationResult result = this.commandDispatcher.sendCommand(
                    CommandNames.REGISTER_COMMAND,
                    RegistrationRequest.builder()
                            .username(registrationDTO.getUsername())
                            .email(registrationDTO.getEmail())
                            .password(registrationDTO.getPassword())
                            .firstName(registrationDTO.getFirstName())
                            .lastName(registrationDTO.getLastName())
                            .dateOfBirth(registrationDTO.getDateOfBirth())
                            .phoneNumber(registrationDTO.getPhoneNumber())
                            .roles(roles)
                            .build(),
                    RegistrationResult.class
            );
            if (result.isSuccessful()) {
                return ResponseEntity.ok().body("Admin created successfully");
            } else {
                return ResponseEntity.badRequest().body(result.getValidationMessages());
            }
        } catch (MessageQueueException e) {
            return commandError(CommandNames.REGISTER_COMMAND, e);
        }
    }

    @Admin
    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable int userId, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            DeleteResult result = this.commandDispatcher.sendCommand(
                    CommandNames.DELETE_ADMIN,
                    DeleteAdminRequest.builder()
                            .username(userDetails.getUsername())
                            .userId(userId)
                            .build(),
                    DeleteResult.class
            );
            if (result.isSuccessful()) {
                return ResponseEntity.ok().body("Admin deleted successfully");
            } else {
                return ResponseEntity.badRequest().body(result.getValidationErrors());
            }
        } catch (MessageQueueException e) {
            return commandError(CommandNames.DELETE_ADMIN, e);
        }
    }

    @Admin
    @GetMapping("/search-users")
    @Cacheable(value = "findAllUsersByFilters", key = "T(java.util.Objects).toString(T(java.util.Objects).hash(#firstName)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#lastName)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#email)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#phoneNumber)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#page)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#size))", sync = true)
    public ResponseEntity<Object> findAllUsersByFilters(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            ProfilesResult result = commandDispatcher.sendCommand(CommandNames.FILTER_PROFILES,
                    FilterUsersRequest.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .phoneNumber(phoneNumber)
                            .page(page)
                            .pageSize(size)
                            .build()
                    ,
                    ProfilesResult.class
            );

            if (!result.isSuccessful()) {
                return ResponseEntity.badRequest().body(result.getErrorMessages());
            }

            evictCache();

            SerializablePage<UserProfileDTO> serializablePage = SerializablePage.of(
                    result.getProfiles(),
                    page,
                    size,
                    result.getTotalNumberOfProfiles(),
                    AdminController::mapUserProfile
            );

            return ResponseEntity.ok(serializablePage);
        } catch (Exception e) {
            return super.commandError(CommandNames.FILTER_PROFILES, e);
        }
    }

    @Scheduled(fixedRate = 100000) // Runs every 100 seconds
    public void evictCacheScheduler() {
        evictCache();
    }

    @CacheEvict(value = "findAllUsersByFilters", allEntries = true)
    public void evictCache() {
        logger.info("Evicting cache");
    }

    @Admin
    @GetMapping("/user-status/{userId}")
    public ResponseEntity<Object> getUserStatus(@PathVariable int userId) {
        try {
            UserStatusResult result = commandDispatcher.sendCommand(
                    CommandNames.USER_STATUS,
                    UserStatusRequest.builder()
                            .userId(userId)
                            .build(),
                    UserStatusResult.class
            );
            if (result.isSuccessful()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body(result.getErrorMessages());
            }
        } catch (MessageQueueException e) {
            return commandError(CommandNames.USER_STATUS, e);
        }
    }

    @Admin
    @PostMapping("/reset-password/{userId}")
    public ResponseEntity<Object> resetPassword(@PathVariable int userId, @RequestBody ResetPasswordDTO resetPasswordDTO) {
        try {
            ChangePasswordResult result = commandDispatcher.sendCommand(
                    CommandNames.RESET_PASSWORD,
                    ResetPasswordRequest.builder()
                            .userId(userId)
                            .password(resetPasswordDTO.getPassword())
                            .build(),
                    ChangePasswordResult.class
            );
            if (result.isSuccessful()) {
                return ResponseEntity.ok().body("Password reset successfully");
            } else {
                return ResponseEntity.badRequest().body(result.getValidationError());
            }
        } catch (MessageQueueException e) {
            return commandError(CommandNames.RESET_PASSWORD, e);
        }
    }

    @Admin
    @PostMapping("/lock-account/{userId}")
    public ResponseEntity<Object> lockAccount(@PathVariable int userId, @RequestBody LockRequestDTO lockRequestDTO) {
        try {
            LockAccountResult result = commandDispatcher.sendCommand(
                    CommandNames.LOCK_ACCOUNT,
                    LockAccountRequest.builder()
                            .userId(userId)
                            .lockoutTime(lockRequestDTO.getLockoutTime())
                            .reason(lockRequestDTO.getReason())
                            .build(),
                    LockAccountResult.class
            );
            if (result.isSuccessful()) {
                return ResponseEntity.ok().body("Account locked successfully");
            } else {
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }
        } catch (MessageQueueException e) {
            return commandError(CommandNames.LOCK_ACCOUNT, e);
        }
    }

    @Admin
    @PostMapping("/unlock-account/{userId}")
    public ResponseEntity<Object> unlockAccount(@PathVariable int userId,
                                                @AuthenticationPrincipal UserDetails userDetails

    ) {
        logger.info("Received unlock request for user with id: {}", userId);
        try {
            LockAccountResult result = commandDispatcher.sendCommand(
                    CommandNames.UNLOCK_ACCOUNT,
                    UnlockAccountRequest.builder()
                            .userId(userId)
                            .build(),
                    LockAccountResult.class
            );
            if (result.isSuccessful()) {
                logger.info("Account with id {} unlocked successfully by admin user {}", userId, userDetails.getUsername());
                return ResponseEntity.ok().body("Account unlocked successfully");
            } else {
                logger.warn("Failed to unlock account with id {} by admin user {}", userId, userDetails.getUsername());
                return ResponseEntity.badRequest().body(result.getErrorMessage());
            }
        } catch (MessageQueueException e) {
            return commandError(CommandNames.UNLOCK_ACCOUNT, e);
        }
    }

}
