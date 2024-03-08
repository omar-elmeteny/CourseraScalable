package profilemanagement.repositories;// UserProfileRepository.java

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import profilemanagement.models.UserProfile;

import java.time.LocalDate;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {



    void deleteByUserId(Long userId);
    @Query(nativeQuery = true, value = "SELECT * FROM find_users_by_filters(" +
            ":pUserId, " +
            ":pFirstName, " +
            ":pLastName, " +
            ":pIsEmailVerified, " +
            ":pIsPhoneVerified, " +
            ":pPhoneNumber, " +
            ":pDateOfBirth)",
            countQuery = "SELECT count(*) FROM find_users_by_filters(" +
                    ":pUserId, " +
                    ":pFirstName, " +
                    ":pLastName, " +
                    ":pIsEmailVerified, " +
                    ":pIsPhoneVerified, " +
                    ":pPhoneNumber, " +
                    ":pDateOfBirth)"
    )
    Page<UserProfile> findUsersByFilters(
            @Param("pUserId") Integer pUserId,
            @Param("pFirstName") String pFirstName,
            @Param("pLastName") String pLastName,
            @Param("pIsEmailVerified") Boolean pIsEmailVerified,
            @Param("pIsPhoneVerified") Boolean pIsPhoneVerified,
            @Param("pPhoneNumber") String pPhoneNumber,
            @Param("pDateOfBirth") LocalDate pDateOfBirth,
            Pageable pageable
    );




}
