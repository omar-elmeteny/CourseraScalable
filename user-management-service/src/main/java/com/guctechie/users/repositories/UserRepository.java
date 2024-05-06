package com.guctechie.users.repositories;


import com.guctechie.users.models.User;
import com.guctechie.users.models.UserStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertUser(User user) {
        //call the function register_user in the database
        String sql = """
                SELECT * FROM register_user(?, ?, ?, ?, ?, ?, ?, ?)
                """;
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getFirstName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getPhoneNumber(),
                user.getProfilePhotoUrl());

        if (rs.next()) {
            user.setUserId(rs.getInt("user_id"));
            user.setRegistrationDate(rs.getDate("registration_date"));
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

    public User findUserByUsername(String username) {
        String sql = """
                SELECT * FROM get_user_by_username(?)
                """;
        List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), username);
        if (users.isEmpty()) {
            return null;
        }
        return users.stream().findFirst().get();
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

    public void updatePassword(User user) {
        //call the procedure change_user_password in the database
        String sql = """
                CALL change_user_password(?, ?)
                """;
        jdbcTemplate.update(sql, user.getUserId(), user.getPasswordHash());
    }

    public void assignRoleToUser(int userId, String role) {
        //call the procedure assign_role in the database
        String sql = """
                CALL assign_role_to_user(?, ?)
                """;
        jdbcTemplate.update(sql, userId, role);
    }

    public void lockAccount(int userId, String reason, Timestamp lockoutTime) {
        //call the procedure lock_account in the database
        String sql = """
                CALL lock_user(?, ?, ?)
                """;
        jdbcTemplate.update(sql, userId, reason, lockoutTime);
    }

    public UserStatus getUserStatus(int userId) {
        String sql = "SELECT * FROM get_user_status(?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
        ArrayList<String> roles = getUserRoles(userId);
        if (rs.next()) {
            return UserStatus.builder()
                    .userId(rs.getInt("user_id"))
                    .username(rs.getString("username"))
                    .isEmailVerified(rs.getBoolean("is_email_verified"))
                    .registrationDate(rs.getDate("registration_date"))
                    .isDeleted(rs.getBoolean("is_deleted"))
                    .isLocked(rs.getBoolean("is_locked"))
                    .lockReason(rs.getString("lock_reason"))
                    .lockoutExpires(rs.getTimestamp("lockout_expires"))
                    .failedLoginCount(rs.getInt("failed_login_count"))
                    .roles(roles)
                    .build();
        }
        return null;
    }

    public User findUserById(int userId) {
        String sql = "SELECT * FROM get_user_by_id(?)";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId);
        if (rs.next()) {
            return User.builder().
                    userId(rs.getInt("user_id")).
                    firstName(rs.getString("first_name")).
                    lastName(rs.getString("last_name")).
                    bio(rs.getString("bio")).
                    profilePhotoUrl(rs.getString("profile_photo_url")).
                    phoneNumber(rs.getString("phone_number")).
                    dateOfBirth(rs.getDate("date_of_birth")).
                    build();
        }
        return  null;
    }

    public void unlockAccount(int userId) {
        //call the procedure unlock_account in the database
        String sql = """
                CALL unlock_user(?)
                """;
        jdbcTemplate.update(sql, userId);
    }


    public static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password_hash"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getDate("date_of_birth"),
                    rs.getDate("registration_date"),
                    rs.getBoolean("is_email_verified"),
                    rs.getString("profile_photo_url"),
                    rs.getString("phone_number"),
                    rs.getString("bio"),
                    rs.getBoolean("is_deleted"),
                    rs.getBoolean("is_locked"),
                    rs.getString("lock_reason"),
                    rs.getTimestamp("lockout_expires"),
                    rs.getInt("failed_login_count")
            );
        }
    }

}
