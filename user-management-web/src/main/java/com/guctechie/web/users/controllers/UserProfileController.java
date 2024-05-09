package com.guctechie.web.users.controllers;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.users.models.*;
import com.guctechie.web.config.InstructorOrStudent;
import com.guctechie.web.users.dtos.UserProfileDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController extends BaseController {

    public UserProfileController(CommandDispatcher commandDispatcher) {
        super();
        this.commandDispatcher = commandDispatcher;
    }

    private final CommandDispatcher commandDispatcher;

    @PutMapping("/update")

    public ResponseEntity<Object> updateUser(
            @RequestBody UserProfileDTO userProfileDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("Updating user profile");
        try {
            UpdateUserProfileResult result = commandDispatcher.sendCommand(CommandNames.UPDATE_USER,
                    UpdateUserProfileRequest.builder()
                            .username(userDetails.getUsername())
                            .userProfileData(UserProfileData.builder()
                                    .firstName(userProfileDTO.getFirstName())
                                    .lastName(userProfileDTO.getLastName())
                                    .phoneNumber(userProfileDTO.getPhoneNumber())
                                    .dateOfBirth(userProfileDTO.getDateOfBirth())
                                    .bio(userProfileDTO.getBio())
                                    .profilePhotoUrl(userProfileDTO.getProfilePhotoUrl())
                                    .build()
                            )
                    , UpdateUserProfileResult.class
            );

            if (result.isSuccessful()) {
                logger.info("User profile updated successfully");
                return ResponseEntity.ok(result);
            }
            else{
                evictCache();
                logger.error("Error updating user profile");
                return ResponseEntity.badRequest().body(result.getErrorMessages());
            }
        } catch (Exception e) {
            logger.error("Command {} failed", CommandNames.UPDATE_USER);
            return super.commandError(CommandNames.UPDATE_USER, e);
        }
    }

    @InstructorOrStudent
    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("Deleting user");
        try {
            DeleteResult result = commandDispatcher.sendCommand(CommandNames.DELETE_USER,
                    DeleteUserRequest.builder()
                            .username(userDetails.getUsername()),
                    DeleteResult.class
            );
            if (result.isSuccessful()) {
                logger.info("User deleted successfully");
                return ResponseEntity.ok(result);
            }
            else{
                evictCache();
                logger.error("Error deleting user");
                return ResponseEntity.badRequest().body(result.getValidationErrors());
            }
        } catch (Exception e) {
            logger.error("Command {} failed", CommandNames.DELETE_USER);
            return super.commandError(CommandNames.DELETE_USER, e);
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

}
