package com.guctechie.web.users.controllers;

import com.guctechie.messages.CommandNames;
import com.guctechie.messages.services.CommandDispatcher;
import com.guctechie.users.models.*;
import com.guctechie.web.users.dtos.UserProfileDTO;
import com.guctechie.web.utils.SerializablePage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/v1/user-profiles")
public class UserProfileController extends BaseController {

    public UserProfileController(CommandDispatcher commandDispatcher) {
        super();
        this.commandDispatcher = commandDispatcher;
    }

    private final CommandDispatcher commandDispatcher;

    private static UserProfileDTO mapUserProfile(UserProfileData userProfileData) {
        return UserProfileDTO.builder()
                .userId(userProfileData.getUserId())
                .profileId(userProfileData.getProfileId())
                .firstName(userProfileData.getFirstName())
                .lastName(userProfileData.getLastName())
                .bio(userProfileData.getBio())
                .profilePhotoUrl(userProfileData.getProfilePhotoUrl())
                .isEmailVerified(userProfileData.isEmailVerified())
                .isPhoneVerified(userProfileData.isPhoneVerified())
                .phoneNumber(userProfileData.getPhoneNumber())
                .dateOfBirth(userProfileData.getDateOfBirth())
                .build();
    }


    @PostMapping
    public ResponseEntity<Object> insertUserProfile(
            @RequestBody UserProfileDTO userProfile
    ) {
        try {
            InsertUserProfileResult result = commandDispatcher.sendCommand(CommandNames.INSERT_PROFILE,
                    UserProfileData.builder()
                            .userId(userProfile.getUserId())
                            .firstName(userProfile.getFirstName())
                            .lastName(userProfile.getLastName())
                            .phoneNumber(userProfile.getPhoneNumber())
                            .dateOfBirth(userProfile.getDateOfBirth())
                            .bio(userProfile.getBio())
                            .profilePhotoUrl(userProfile.getProfilePhotoUrl())
                            .build()

                    , InsertUserProfileResult.class
            );

            if (!result.isSuccessful()) {
                return ResponseEntity.badRequest().body(result.getErrorMessages());
            }
            evictCache();
            return ResponseEntity.ok(result.getProfileId());
        } catch (Exception e) {
            return super.commandError(CommandNames.INSERT_PROFILE);
        }
    }

    @PutMapping("/{profileId}")

    public ResponseEntity<Object> updateUserProfile(
            @PathVariable Integer profileId,
            @RequestBody UserProfileDTO userProfile,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            UpdateUserProfileResult result = commandDispatcher.sendCommand(CommandNames.UPDATE_PROFILE,
                    UpdateUserProfileRequest.builder()
                            .profileId(profileId)
                            .username(userDetails.getUsername())
                            .userProfileData(UserProfileData.builder()
                                    .firstName(userProfile.getFirstName())
                                    .lastName(userProfile.getLastName())
                                    .phoneNumber(userProfile.getPhoneNumber())
                                    .dateOfBirth(userProfile.getDateOfBirth())
                                    .bio(userProfile.getBio())
                                    .profilePhotoUrl(userProfile.getProfilePhotoUrl())
                                    .emailVerified(userProfile.isEmailVerified())
                                    .phoneVerified(userProfile.isEmailVerified())
                                    .build()
                            )
                    , UpdateUserProfileResult.class
            );

            if (!result.isSuccessful()) {
                return ResponseEntity.badRequest().body(result.getErrorMessages());
            }
            evictCache();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return super.commandError(CommandNames.UPDATE_PROFILE);
        }
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Object> deleteUserProfile(
            @PathVariable Integer profileId
    ) {
        try {
            DeleteProfileResult result = commandDispatcher.sendCommand(CommandNames.DELETE_PROFILE_BY_ID,
                    DeleteProfileRequest.builder()
                            .profileId(profileId)
                            .build(),
                    DeleteProfileResult.class
            );
            if (!result.isSuccessful()) {
                return ResponseEntity.badRequest().body("Profile not found");
            }
            evictCache();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return super.commandError(CommandNames.DELETE_PROFILE_BY_ID);
        }
    }

    @DeleteMapping("/user/{userId}")

    public ResponseEntity<Object> deleteUserProfileByUser(
            @PathVariable int userId
    ) {
        try {
            DeleteProfileResult result = commandDispatcher.sendCommand(CommandNames.DELETE_PROFILE_BY_USER_ID,
                    DeleteProfileByUserIdRequest.builder()
                            .userId(userId),
                    DeleteProfileResult.class
            );
            if (!result.isSuccessful()) {
                return ResponseEntity.badRequest().body("No profiles found for the user");
            }
            evictCache();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return super.commandError(CommandNames.DELETE_PROFILE_BY_USER_ID);
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


    @GetMapping
    @Cacheable(value = "findAllUsersByFilters", key = "T(java.util.Objects).toString(T(java.util.Objects).hash(#userId)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#firstName)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#lastName)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#page)) + '-' + " +
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#size))", sync = true)
    public ResponseEntity<Object> findAllUsersByFilters(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        LocalDate dateOfBirthDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .parse(dateOfBirth, LocalDate::from);
        try {
            ProfilesResult result = commandDispatcher.sendCommand(CommandNames.FILTER_PROFILES,
                    FilterProfilesRequest.builder()
                            .userId(userId)
                            .firstName(firstName)
                            .lastName(lastName)
                            .phoneNumber(phoneNumber)
                            .dateOfBirth(dateOfBirthDate)
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
                    UserProfileController::mapUserProfile
            );

            return ResponseEntity.ok(serializablePage);
        } catch (Exception e) {
            return super.commandError(CommandNames.FILTER_PROFILES);
        }
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<Object> findUserProfileByProfileId(@PathVariable Integer profileId) {

        try {
            UserProfileData result = commandDispatcher.sendCommand(CommandNames.GET_PROFILE_BY_ID,
                    ProfileByIdRequest.builder()
                            .profileId(profileId)
                            .build()
                    ,
                    UserProfileData.class
            );
            if (result == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(mapUserProfile(result));
        } catch (Exception e) {
            return super.commandError(CommandNames.GET_PROFILE_BY_ID);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> findUserProfileByUserId(@PathVariable Integer userId) {
        try {
            ProfilesResult result = commandDispatcher.sendCommand(CommandNames.GET_PROFILE_BY_USER_ID,
                    ProfileByIdRequest.builder()
                            .profileId(userId)
                            .build()
                    ,
                    ProfilesResult.class
            );
            if (result.getProfiles().isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var mappedProfiles = result.getProfiles().stream()
                    .map(UserProfileController::mapUserProfile)
                    .toList();
            return ResponseEntity.ok(mappedProfiles);
        } catch (Exception e) {
            return super.commandError(CommandNames.GET_PROFILE_BY_USER_ID);
        }
    }
}
