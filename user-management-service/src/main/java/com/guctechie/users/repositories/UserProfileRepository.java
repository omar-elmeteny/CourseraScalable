package com.guctechie.users.repositories;

import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.models.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserProfileRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    public void deleteUserByUserId(int userId) {
        String sql = "SELECT delete_user(?)";
        jdbcTemplate.update(sql, userId);
    }

    public void update(int userId, UserProfileData updatedUserProfile) {
        String sql = "SELECT update_user_profile(?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                userId,
                updatedUserProfile.getUserId(),
                updatedUserProfile.getFirstName(),
                updatedUserProfile.getLastName(),
                updatedUserProfile.getBio(),
                updatedUserProfile.getProfilePhotoUrl(),
                updatedUserProfile.getPhoneNumber(),
                updatedUserProfile.getDateOfBirth()
        );

    }

    public UserProfileData findUserProfileByPhone(
            String pPhone
    ) {
        String sql = "SELECT * FROM get_user_by_phone(?)";

        List<UserProfileData> userProfiles = jdbcTemplate.query(
                sql,
                new UserProfileMapper(),
                pPhone
        );

        return !userProfiles.isEmpty() ? userProfiles.get(0) : null;
    }

    public int countUsersByFilters(
            String pFirstName,
            String pLastName,
            String pEmail,
            String pPhoneNumber
    ) {
        String sql = "SELECT * FROM find_count_by_filters(?, ?, ?, ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, new UserProfileMapper(), pFirstName, pLastName, pEmail, pPhoneNumber);
        if (rs.next()) {
            return rs.getInt(0);
        }
        return 0;
    }

    public List<UserProfileData> findUsersByFilters(
            String pFirstName,
            String pLastName,
            String pEmail,
            String pPhoneNumber,
            int limit,
            int offset
    ) {
        String sql = "SELECT * FROM find_users_by_filters(?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.query(sql, new UserProfileMapper(), pFirstName, pLastName, pEmail, pPhoneNumber, limit, offset);
    }

    private static class UserProfileMapper implements RowMapper<UserProfileData> {
        @Override
        public UserProfileData mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            return new UserProfileData(
                    resultSet.getInt("user_id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("bio"),
                    resultSet.getString("profile_photo_url"),
                    resultSet.getString("phone_number"),
                    resultSet.getDate("date_of_birth")
            );
        }
    }
}