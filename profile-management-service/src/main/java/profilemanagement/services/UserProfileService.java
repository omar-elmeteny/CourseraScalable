package profilemanagement.services;// UserProfileService.java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import profilemanagement.models.UserProfile;
import profilemanagement.repositories.UserProfileRepository;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    public void insertUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public void updateUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public void deleteUserProfile(Long profileId) {
        userProfileRepository.deleteById(profileId);
    }

    public UserProfile getUserProfileById(Long profileId) {
        return userProfileRepository.findById(profileId).orElse(null);
    }

    public UserProfile getUserProfileByUserId(Long userId) {
        // Implement the method to find by user ID
        // You can use userProfileRepository.findByUserId(userId);
        return null;
    }
}
