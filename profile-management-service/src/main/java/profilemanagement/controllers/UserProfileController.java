package profilemanagement.controllers;// UserProfileController.java

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import profilemanagement.models.UserProfile;
import profilemanagement.services.UserProfileService;

@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    @Autowired

    private final UserProfileService userProfileService;



    @PostMapping
    public ResponseEntity<Void> insertUserProfile(@RequestBody UserProfile userProfile) {
        userProfileService.insertUserProfile(userProfile);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<Void> updateUserProfile(
            @PathVariable Long profileId,
            @RequestBody UserProfile userProfile
    ) {
        userProfile.setProfileId(profileId);
        userProfileService.updateUserProfile(userProfile);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteUserProfile(@PathVariable Long profileId) {
        userProfileService.deleteUserProfile(profileId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<UserProfile> getUserProfileById(@PathVariable Long profileId) {
        UserProfile userProfile = userProfileService.getUserProfileById(profileId);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfile> getUserProfileByUserId(@PathVariable Long userId) {
        UserProfile userProfile = userProfileService.getUserProfileByUserId(userId);
        return ResponseEntity.ok(userProfile);
    }
}
