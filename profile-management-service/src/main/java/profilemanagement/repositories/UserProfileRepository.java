package profilemanagement.repositories;// UserProfileRepository.java

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import profilemanagement.models.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Page<UserProfile> findByUsers();
    UserProfile findByUserId(Long userId);

    UserProfile findByPhoneNumber(String phoneNumber);

    UserProfile findByEmail(String email);

    UserProfile findByEmailOrPhoneNumber(String email, String phoneNumber);


}
