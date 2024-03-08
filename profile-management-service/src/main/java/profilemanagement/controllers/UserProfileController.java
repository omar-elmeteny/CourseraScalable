package profilemanagement.controllers;// UserProfileController.java

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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



    @GetMapping
    public ResponseEntity<Page<UserProfile>> findAllUsersByFilters(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean isEmailVerified,
            @RequestParam(required = false) Boolean isPhoneVerified,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<UserProfile> users = userProfileService.findAllUsersByFilters(
                userId,
                firstName, lastName, isEmailVerified,isPhoneVerified,phoneNumber,dateOfBirth, Pageable.ofSize(size).withPage(page)
        );
        return ResponseEntity.ok(users);
    }
}
