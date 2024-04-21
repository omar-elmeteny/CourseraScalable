package profilemanagement.repositories;

import profilemanagement.dao.UserProfileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import profilemanagement.models.UserProfile;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UserProfileRepository {
    private final Logger logger = LoggerFactory.getLogger(UserProfileRepository.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_profile WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }


    public void deleteByProfileId(Long profileId){
        String sql = "DELETE FROM user_profile WHERE profile_id = ?";
        jdbcTemplate.update(sql, profileId);
    }

    public void save(UserProfile userProfile) {
        String sql = "INSERT INTO user_profile (user_id, first_name, last_name, is_email_verified, is_phone_verified, phone_number, date_of_birth) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {
                userProfile.getUserId(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getIsEmailVerified(),
                userProfile.getIsPhoneVerified(),
                userProfile.getPhoneNumber(),
                userProfile.getDateOfBirth()
        };

        int[] types = {
                Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR, Types.DATE
        };

        jdbcTemplate.update(sql, params, types);
    }

    public void update(UserProfile updatedUserProfile){
String sql = "UPDATE user_profile SET first_name = ?, last_name = ?, bio = ?, is_email_verified = ?, is_phone_verified = ?, phone_number = ?, date_of_birth = ? WHERE user_id = ?";
        Object[] params = {
                updatedUserProfile.getFirstName(),
                updatedUserProfile.getLastName(),
                updatedUserProfile.getBio(),
                updatedUserProfile.getIsEmailVerified(),
                updatedUserProfile.getIsPhoneVerified(),
                updatedUserProfile.getPhoneNumber(),
                updatedUserProfile.getDateOfBirth(),
                updatedUserProfile.getUserId()
        };
        int[] types = {
                Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR, Types.DATE, Types.BIGINT
        };
        jdbcTemplate.update(sql, params, types);

    }

    public void insertUserProfile(UserProfileRequest userProfile) {
        String sql = "INSERT INTO user_profile (user_id, first_name, last_name, is_email_verified, is_phone_verified, phone_number, date_of_birth) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Object[] params = {
                userProfile.getUserId(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getIsEmailVerified(),
                userProfile.getIsPhoneVerified(),
                userProfile.getPhoneNumber(),
                userProfile.getDateOfBirth()
        };

        int[] types = {
                Types.BIGINT, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR, Types.DATE
        };



        jdbcTemplate.update(sql, params, types);

    }

    public void updateUserProfile(UserProfile userProfile) {
        String sql = "UPDATE user_profile SET first_name = ?, last_name = ?, is_email_verified = ?, " +
                "is_phone_verified = ?, phone_number = ?, date_of_birth = ? WHERE user_id = ?";

        Object[] params = {
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getIsEmailVerified(),
                userProfile.getIsPhoneVerified(),
                userProfile.getPhoneNumber(),
                userProfile.getDateOfBirth(),
                userProfile.getUserId()
        };

        int[] types = {
                Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR, Types.DATE, Types.BIGINT
        };

        jdbcTemplate.update(sql, params, types);
    }

    public void deleteUserProfile(Long userId) {
        String sql = "DELETE FROM user_profile WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    public UserProfile findUserProfileByUserId(
            Integer pUserId
    ) {
        String sql = "SELECT * FROM find_user_profile_by_user_id(CAST(? AS INTEGER))";

        Object[] params = {
                pUserId
        };

        int[] types = {
                Types.INTEGER        };

        List<UserProfile> userProfiles = jdbcTemplate.query(
                sql,
                params,
                types,
                new UserProfileMapper()
        );


        return !userProfiles.isEmpty() ? userProfiles.get(0) : null;
    }

    public UserProfile findUserProfileByProfileId(
            Integer pProfileId
    ) {
        String sql = "SELECT * FROM find_user_profile_by_profile_id(CAST(? AS INTEGER))";

        Object[] params = {
                pProfileId
        };

        int[] types = {
                Types.INTEGER
        };

        List<UserProfile> userProfiles = jdbcTemplate.query(
                sql,
                params,
                types,
                new UserProfileMapper()
        );




        return !userProfiles.isEmpty() ? userProfiles.get(0) : null;
    }

    public Page<UserProfile> findUsersByFilters(
            Integer pUserId,
            String pFirstName,
            String pLastName,
            Boolean pIsEmailVerified,
            Boolean pIsPhoneVerified,
            String pPhoneNumber,
            LocalDate pDateOfBirth,
            Pageable pageable
    ) {
        String sql = "SELECT * FROM find_users_by_filters(CAST(? AS INTEGER), CAST(? AS VARCHAR), CAST(? AS VARCHAR), CAST(? AS BOOLEAN), CAST(? AS BOOLEAN), CAST(? AS VARCHAR), CAST(? AS DATE))";

        Object[] params = {
                pUserId, pFirstName, pLastName, pIsEmailVerified, pIsPhoneVerified, pPhoneNumber, pDateOfBirth
        };

        int[] types = {
                Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.BOOLEAN, Types.BOOLEAN, Types.VARCHAR, Types.DATE
        };

        List<UserProfile> userProfiles = jdbcTemplate.query(
                sql,
                params,
                types,
                new UserProfileMapper()
        );
        int start = Math.min( (int) pageable.getOffset() ,Math.max( userProfiles.size()-1,0));
        int end = Math.min((start + pageable.getPageSize()), userProfiles.size());

        // Slice the list based on pagination parameters
        List<UserProfile> slicedUserProfiles = userProfiles.subList(start, end);


        return new PageImpl<UserProfile>(slicedUserProfiles, pageable, userProfiles.size());
    }



    private static class UserProfileMapper implements RowMapper<UserProfile> {
        @Override
        public UserProfile mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new UserProfile(
                    resultSet.getLong("profile_id"),
                    resultSet.getLong("user_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("bio"),
                    resultSet.getString("profile_photo_url"),
                    resultSet.getBoolean("is_email_verified"),
                    resultSet.getBoolean("is_phone_verified"),
                    resultSet.getString("phone_number"),
                    // typecast it to string
                    resultSet.getString("date_of_birth")
            );
        }
    }
}











//package profilemanagement.repositories;// UserProfileRepository.java
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import profilemanagement.models.UserProfile;
//
//import java.time.LocalDate;
//
//public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
//
//
//
//    void deleteByUserId(Long userId);
//    @Query(nativeQuery = true, value = "SELECT * FROM find_users_by_filters(" +
//            ":pUserId, " +
//            ":pFirstName, " +
//            ":pLastName, " +
//            ":pIsEmailVerified, " +
//            ":pIsPhoneVerified, " +
//            ":pPhoneNumber, " +
//            ":pDateOfBirth)",
//            countQuery = "SELECT count(*) FROM find_users_by_filters(" +
//                    ":pUserId, " +
//                    ":pFirstName, " +
//                    ":pLastName, " +
//                    ":pIsEmailVerified, " +
//                    ":pIsPhoneVerified, " +
//                    ":pPhoneNumber, " +
//                    ":pDateOfBirth)"
//    )
//    Page<UserProfile> findUsersByFilters(
//            @Param("pUserId") Integer pUserId,
//            @Param("pFirstName") String pFirstName,
//            @Param("pLastName") String pLastName,
//            @Param("pIsEmailVerified") Boolean pIsEmailVerified,
//            @Param("pIsPhoneVerified") Boolean pIsPhoneVerified,
//            @Param("pPhoneNumber") String pPhoneNumber,
//            @Param("pDateOfBirth") LocalDate pDateOfBirth,
//            Pageable pageable
//    );
//
//
//
//
//}



