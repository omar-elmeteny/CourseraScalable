package com.guctechie.users.repositories;

import com.guctechie.users.models.UserProfileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class UserProfileRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteByProfileId(int profileId) {
        String sql = "SELECT delete_user_profile(?)";
        jdbcTemplate.update(sql, profileId);
    }

    public void update(UserProfileData updatedUserProfile) {
        String sql = "SELECT update_user_profile(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                updatedUserProfile.getProfileId(),
                updatedUserProfile.getFirstName(),
                updatedUserProfile.getLastName(),
                updatedUserProfile.getBio(),
                updatedUserProfile.getProfilePhotoUrl(),
                updatedUserProfile.isEmailVerified(),
                updatedUserProfile.isPhoneVerified(),
                updatedUserProfile.getPhoneNumber(),
                updatedUserProfile.getDateOfBirth()
        );

    }

    public int insertUserProfile(UserProfileData userProfile) throws Exception {
        String sql = "SELECT * FROM insert_user_profile(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,
                sql,
                userProfile.getUserId(),
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getBio(),
                userProfile.getProfilePhotoUrl(),
                false,
                false,
                userProfile.getPhoneNumber(),
                userProfile.getDateOfBirth()
        );
        if (rowSet.next()) {
            return rowSet.getInt(0);
        }
        throw new Exception("User profiled id not returned from the database");
    }

    public UserProfileData findUserProfileByUserId(
            int pUserId
    ) {
        String sql = "SELECT * FROM find_user_profile_by_user_id(?)";

        List<UserProfileData> userProfiles = jdbcTemplate.query(
                sql,
                new UserProfileMapper(),
                pUserId
        );
        return !userProfiles.isEmpty() ? userProfiles.get(0) : null;
    }

    public UserProfileData findUserProfileByPhone(
            String pPhone
    ) {
        String sql = "SELECT * FROM get_user_profile_phone(?)";

        List<UserProfileData> userProfiles = jdbcTemplate.query(
                sql,
                new UserProfileMapper(),
                pPhone
        );

        return !userProfiles.isEmpty() ? userProfiles.get(0) : null;
    }

    public UserProfileData findUserProfileByProfileId(
            int pProfileId
    ) {
        String sql = "SELECT * FROM find_user_profile_by_profile_id(?)";

        List<UserProfileData> userProfiles = jdbcTemplate.query(
                sql,
                new UserProfileMapper(),
                pProfileId
        );
        return !userProfiles.isEmpty() ? userProfiles.get(0) : null;
    }

    public int countUsersByFilters(
            Integer pUserId,
            String pFirstName,
            String pLastName,
            Boolean pIsEmailVerified,
            Boolean pIsPhoneVerified,
            String pPhoneNumber,
            LocalDate pDateOfBirth
    ) {
        String sql = "SELECT * FROM find_count_by_filters(?, ?, ?, ?, ?, ?, ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, new UserProfileMapper(), pUserId, pFirstName, pLastName, pIsEmailVerified, pIsPhoneVerified, pPhoneNumber, pDateOfBirth);
        if (rs.next()) {
            return rs.getInt(0);
        }
        return 0;
    }

    public List<UserProfileData> findUsersByFilters(
            Integer pUserId,
            String pFirstName,
            String pLastName,
            Boolean pIsEmailVerified,
            Boolean pIsPhoneVerified,
            String pPhoneNumber,
            LocalDate pDateOfBirth,
            int limit,
            int offset
    ) {
        String sql = "SELECT * FROM find_users_by_filters(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.query(sql, new UserProfileMapper(), pUserId, pFirstName, pLastName, pIsEmailVerified, pIsPhoneVerified, pPhoneNumber, pDateOfBirth, limit, offset);
    }


    private static class UserProfileMapper implements RowMapper<UserProfileData> {
        @Override
        public UserProfileData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new UserProfileData(
                    resultSet.getInt("profile_id"),
                    resultSet.getInt("user_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("bio"),
                    resultSet.getString("profile_photo_url"),
                    resultSet.getBoolean("is_email_verified"),
                    resultSet.getBoolean("is_phone_verified"),
                    resultSet.getString("phone_number"),
                    resultSet.getDate("date_of_birth")
            );
        }
    }
}