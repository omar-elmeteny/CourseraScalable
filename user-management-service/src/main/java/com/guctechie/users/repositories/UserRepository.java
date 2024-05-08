package com.guctechie.users.repositories;


import com.guctechie.users.models.UserProfileData;
import com.guctechie.users.models.UserStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUser(UserProfileData user, String passwordHash) {
        //call the function register_user in the database
        String sql = """
                SELECT * FROM register_user(?, ?, ?, ?, ?, ?, ?, ?)
                """;
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                user.getUsername(),
                user.getEmail(),
                passwordHash,
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getDateOfBirth(),
                user.getProfilePhotoUrl());

        if (rs.next()) {
            user.setUserId(rs.getInt("user_id"));
        }
    }

    public boolean usernameExists(String username) {
        String sql = """
                SELECT * FROM get_user_by_username(?)
                """;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        return rowSet.next();
    }

    public boolean emailExists(String email) {
        String sql = """
                SELECT * FROM get_user_by_email(?)
                """;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, email);
        return rowSet.next();
    }

    public boolean phoneExists(String phoneNumber) {
        String sql = """
                SELECT * FROM get_user_by_phone(?)
                """;
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, phoneNumber);
        return rowSet.next();
    }

    public UserProfileData findUserByUsername(String username) {
        String sql = """
                SELECT * FROM get_user_by_username(?)
                """;
        List<UserProfileData> users = jdbcTemplate.query(sql, UserProfileDataMapper.INSTANCE, username);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public UserProfileData findUserByEmail(String email) {
        String sql = """
                SELECT * FROM get_user_by_email(?)
                """;
        List<UserProfileData> users = jdbcTemplate.query(sql, UserProfileDataMapper.INSTANCE, email);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public UserProfileData findUserByPhone(String phoneNumber) {
        String sql = """
                SELECT * FROM get_user_by_phone(?)
                """;
        List<UserProfileData> users = jdbcTemplate.query(sql, UserProfileDataMapper.INSTANCE, phoneNumber);
        if (users.isEmpty()) {
            return null;
        }
        return users.get(0);
    }

    public ArrayList<String> getUserRoles(int userId) {
        String sql = """
                SELECT * FROM get_user_roles(?)
                """;
        return new ArrayList<>(jdbcTemplate.queryForList(sql, String.class, userId));
    }

    public void removeRoleFromUser(int userId, String role) {
        //call the procedure remove_role in the database
        String sql = """
                CALL remove_role_from_user(?, ?)
                """;
        jdbcTemplate.update(sql, userId, role);
    }

    public void updatePassword(int userId, String passwordHash) {
        //call the procedure change_user_password in the database
        String sql = """
                CALL change_user_password(?, ?)
                """;
        jdbcTemplate.update(sql, userId, passwordHash);
    }

    public void assignRoleToUser(int userId, String role) {
        //call the procedure assign_role in the database
        String sql = """
                CALL assign_role_to_user(?, ?)
                """;
        jdbcTemplate.update(sql, userId, role);
    }

    public void lockAccount(int userId, String reason, Date lockoutTime) {
        //call the procedure lock_account in the database
        String sql = """
                CALL lock_user(?, ?, ?)
                """;
        jdbcTemplate.update(sql, userId, reason, lockoutTime);
    }

    public void createPasswordResetRequest(int userId, String otp, Date expires) {
        //call the procedure create_password_reset_request in the database
        String sql = """
                CALL create_password_reset_request(?, ?, ?)
                """;
        jdbcTemplate.update(sql, userId, otp, expires);
    }

    public UserStatus getUserStatus(int userId) {
        String sql = "SELECT * FROM get_user_status(?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
        ArrayList<String> roles = getUserRoles(userId);
        if (rs.next()) {
            return UserStatus.builder()
                    .userId(rs.getInt("user_id"))
                    .username(rs.getString("username"))
                    .locked(rs.getBoolean("is_locked"))
                    .deleted(rs.getBoolean("is_deleted"))
                    .failedLoginCount(rs.getInt("failed_login_count"))
                    .lockReason(rs.getString("lock_reason"))
                    .lockoutExpires(rs.getTimestamp("lockout_expires"))
                    .emailVerified(rs.getBoolean("is_email_verified"))
                    .registrationDate(rs.getDate("registration_date"))
                    .passwordDate(rs.getDate("password_date"))
                    .passwordHash(rs.getString("password_hash"))
                    .roles(roles)
                    .build();
        }
        return null;
    }

    public int addLoginAttempt(int userId, boolean status, String ipAddress, String userAgent) {
        String sql = """
                SELECT add_login_attempt(?, ?, ?, ?)
                """;
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId, status, ipAddress, userAgent);
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public UserProfileData findUserById(int userId) {
        String sql = "SELECT * FROM get_user_by_id(?)";
        List<UserProfileData> result = jdbcTemplate.query(sql, UserProfileDataMapper.INSTANCE, userId);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);

    }

    public void unlockAccount(int userId) {
        //call the procedure unlock_account in the database
        String sql = """
                CALL unlock_user(?)
                """;
        jdbcTemplate.update(sql, userId);
    }


    public void verifyEmail(int userId, boolean isCorrect) {
        //call the procedure verify_email in the database
        String sql = """
                CALL set_email_verified(?)
                """;
        jdbcTemplate.update(sql, userId);
    }

    public void handleWrongOTP(int userId) {
        //call the procedure handle_wrong_otp in the database
        String sql = """
                CALL handle_wrong_otp(?)
                """;
        jdbcTemplate.update(sql, userId);
    }
}
