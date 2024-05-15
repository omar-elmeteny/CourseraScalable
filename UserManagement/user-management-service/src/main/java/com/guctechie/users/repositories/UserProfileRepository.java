package com.guctechie.users.repositories;

import com.guctechie.users.models.UserProfileData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
                UserProfileDataMapper.INSTANCE,
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
        String sql = "SELECT find_count_by_filters(?, ?, ?, ?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, pFirstName, pLastName, pEmail, pPhoneNumber);
        if (rs.next()) {
            return rs.getInt(1);
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
        return jdbcTemplate.query(sql, UserProfileDataMapper.INSTANCE, pFirstName, pLastName, pPhoneNumber, pEmail, limit, offset);
    }
}