package profilemanagement.controllers;// UserProfileController.java

import profilemanagement.dao.UserProfileRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import profilemanagement.models.UserProfile;
import profilemanagement.services.UserProfileService;
import profilemanagement.utils.SerializablePage;

@RestController
@RequestMapping("/api/v1/user-profiles")
@RequiredArgsConstructor
public class UserProfileController {
    @Autowired

    private final UserProfileService userProfileService;

    private final Logger logger = LoggerFactory.getLogger(UserProfileController.class);




    @PostMapping
    public ResponseEntity insertUserProfile(@RequestBody UserProfileRequest userProfile) {
        try {
        userProfileService.insertUserProfile(userProfile);
        evictCache();
            return ResponseEntity.ok("User profile inserted successfully");
        } catch (DataIntegrityViolationException e) {
            // Handle specific constraint violation errors
            String errorMessage = userProfileService.handleConstraintViolationException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            // Handle other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{profileId}")

    public ResponseEntity updateUserProfile(
            @PathVariable Integer profileId,
            @RequestBody UserProfile userProfile
    ) {

        try {
            if(userProfileService.updateProfile(profileId, userProfile)){
                evictCache();
                return ResponseEntity.ok("User profile updated successfully");
            }
            else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found");
            }
        }
        catch (DataIntegrityViolationException e) {
            // Handle specific constraint violation errors
            String errorMessage = userProfileService.handleConstraintViolationException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        } catch (Exception e) {
            // Handle other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @DeleteMapping("/{profileId}")

    public ResponseEntity deleteUserProfile(@PathVariable Integer profileId) {
        try {
            userProfileService.deleteUserProfile(profileId);
            evictCache();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }




        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")

    public ResponseEntity deleteUserProfileByUser(@PathVariable Integer userId) {
        try {
            userProfileService.deleteUserProfileByUserId(userId);
            evictCache();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }




        return ResponseEntity.noContent().build();
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
            "T(java.util.Objects).toString(T(java.util.Objects).hash(#size))" , sync = true)   public SerializablePage<UserProfile> findAllUsersByFilters(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean isEmailVerified,
            @RequestParam(required = false) Boolean isPhoneVerified,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String dateOfBirth,
            @RequestParam(defaultValue = "0") String page,
            @RequestParam(defaultValue = "10") String size) {

        if(StringUtils.hasText(dateOfBirth) && !dateOfBirth.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd");
        }

        else if(StringUtils.hasText(phoneNumber) && !phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("Invalid phone number format. Please use 10 digits");
        }
        else if(StringUtils.hasText(firstName) && firstName.length() > 50) {
            throw new IllegalArgumentException("First name is too long. Max length is 50 characters");
        }
        else if(StringUtils.hasText(lastName) && lastName.length() > 50) {
            throw new IllegalArgumentException("Last name is too long. Max length is 50 characters");
        }
        else if(StringUtils.hasText(phoneNumber) && phoneNumber.length() > 10) {
            throw new IllegalArgumentException("Phone number is too long. Max length is 10 characters");
        }
        else if(StringUtils.hasText(dateOfBirth) && dateOfBirth.length() > 10) {
            throw new IllegalArgumentException("Date of birth is too long. Max length is 10 characters");
        }
        else if(StringUtils.hasText(page) && !page.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid page number. Please use a positive integer");
        }
        else if(StringUtils.hasText(size) && !size.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid page size. Please use a positive integer");
        }
        else if(StringUtils.hasText(firstName) && firstName.length() > 50) {
            throw new IllegalArgumentException("First name is too long. Max length is 50 characters");
        }
        else if(StringUtils.hasText(lastName) && lastName.length() > 50) {
            throw new IllegalArgumentException("Last name is too long. Max length is 50 characters");
        }
        else if(StringUtils.hasText(phoneNumber) && phoneNumber.length() > 10) {
            throw new IllegalArgumentException("Phone number is too long. Max length is 10 characters");
        }
        else if(StringUtils.hasText(dateOfBirth) && dateOfBirth.length() > 10) {
            throw new IllegalArgumentException("Date of birth is too long. Max length is 10 characters");
        }
        else if(StringUtils.hasText(page) && !page.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid page number. Please use a positive integer");
        }
        else if(StringUtils.hasText(size) && !size.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid page size. Please use a positive integer");
        }
        else if(StringUtils.hasText(firstName) && firstName.length() > 50) {
            throw new IllegalArgumentException("First name is too long. Max length is 50 characters");
        }
        else if(StringUtils.hasText(lastName) && lastName.length() > 50) {
            throw new IllegalArgumentException("Last name is too long. Max length is 50 characters");
        }

        // if page and size are not numbers
        if(!page.matches("\\d+") || !size.matches("\\d+")) {
            throw new IllegalArgumentException("Invalid page number or size. Please use a positive integer");
        }



        Page<UserProfile> users = userProfileService.findAllUsersByFilters(
                userId,
                firstName, lastName, isEmailVerified,isPhoneVerified,phoneNumber,dateOfBirth, Pageable.ofSize(Integer.parseInt(size)).withPage(Integer.parseInt(page))
        );

        return
                new SerializablePage(users)
              ;
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<UserProfile> findUserProfileByProfileId(@PathVariable Integer profileId) {

        UserProfile userProfile = userProfileService.findUserProfileByProfileId(profileId);

        if(userProfile != null) {
            return ResponseEntity.ok(userProfile);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserProfile>  findUserProfileByUserId(@PathVariable Integer userId) {
        UserProfile userProfile=  userProfileService.findUserProfileByUserId(userId);
        if(userProfile != null) {
            return ResponseEntity.ok(userProfile);
        }
        return ResponseEntity.notFound().build();
    }



}
